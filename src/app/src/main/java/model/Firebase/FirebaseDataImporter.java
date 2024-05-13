package model.Firebase;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.List;


public class FirebaseDataImporter<T> {
    private FirebaseDatabase database;

    public FirebaseDataImporter() {
        this.database = FirebaseDatabase.getInstance();
    }

    // 泛型方法用于导入JSON字符串到Firebase Realtime Database
    public void importJsonToFirebase(List<T> data, String path) {
        DatabaseReference ref = database.getReference(path);

        if (data != null) {
            for (T item : data) {
                String id = ref.push().getKey();
                if (id != null) {
                    ref.child(id).setValue(item).addOnSuccessListener(aVoid ->
                                    Log.d("FirebaseDataImporter", "Data successfully written to path: " + path))
                            .addOnFailureListener(e ->
                                    Log.e("FirebaseDataImporter", "Failed to write data to path: " + path, e));
                }
            }
        } else {
            Log.e("FirebaseDataImporter", "No data to upload");
        }
    }

    // 泛型方法用于导入对象列表到Firebase Firestore
    public void importJsonToFirestore(List<T> items, String collectionName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        int[] successCount = new int[1];

        for (T item : items) {
            db.collection(collectionName).add(item)
                    .addOnSuccessListener(documentReference -> {
                        successCount[0]++;
                        Log.d("Firestore", "DocumentSnapshot added with ID: " + documentReference.getId());
                        if (successCount[0] == items.size()) {
                            Log.d("Firestore", "All documents uploaded successfully. Total: " + successCount[0]);
                        }
                    })
                    .addOnFailureListener(e ->
                            Log.w("Firestore", "Error adding document", e));
        }
    }
}