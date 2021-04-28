package fr.adlan.naturecollection

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import fr.adlan.naturecollection.PlantRepository.Singleton.databaseRef
import fr.adlan.naturecollection.PlantRepository.Singleton.downloadUri
import fr.adlan.naturecollection.PlantRepository.Singleton.plantList
import fr.adlan.naturecollection.PlantRepository.Singleton.storageReference
import java.util.*


class PlantRepository {
// je suis sur br1
    object Singleton {
        // donner le lien pour acceder au BUCKET
        private val BUCKET_URL: String = "gs://nature-collection-58f21.appspot.com"

        // se connecter à espace de stockage Firebase
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)

        // se connecter à la référence "plants"
        val databaseRef = FirebaseDatabase.getInstance().getReference("plants")

        // creer une liste qui va contenir nos plantes
        val plantList = arrayListOf<PlantModel>()

        // contenir le lien image courante
        var downloadUri: Uri? = null
    }

    fun updateData(callback: () -> Unit) {
        // absorber les données récupérer dans la DatabaseRef
        // et les mettre --> liste de plante
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                // retirer les anciennes données avant la mise à jour (sinon doublon)
                plantList.clear()
                // récolter la liste
                for (ds in snapshot.children) {
                    // construire un objet plante
                    val plant = ds.getValue(PlantModel::class.java)
                    // vérifier si plante non null
                    if (plant != null) {
                        // ajouter la plante a la liste
                        plantList.add(plant)
                    }
                }
                //actionner le callback
                callback()
            }

        })
    }

    // creer fonction envoie fichiers sur storage
    fun uploadImage(file: Uri, callback: () -> Unit) {
        // verifier que fichier non null
        if (file != null) {
            val fileName = UUID.randomUUID().toString() + ".jpg"
            val ref = storageReference.child(fileName)
            val uploadTask = ref.putFile(file)

                Log.d("lol", "uploadImage: " + fileName)

            // demarrer tache d'envoi
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                // verifier si probleme lors de l'envoie fichier
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                    Log.d("lol", "uploadImage: verification erreur lors de l'envoie fichier")
                }
                return@Continuation ref.downloadUrl

            }).addOnCompleteListener { task ->
                // verifier si tout a bien fonctionner
                Log.d(
                    "lol",
                    "uploadImage: verification 1 si tout est OK lors de l'envoie fichier"
                )
                if (task.isSuccessful) {
                    //recuperer image
                    downloadUri = task.result
                    Log.d(
                        "lol",
                        "uploadImage: verification 2 si tout est OK lors de l'envoie fichier"
                    )
                    callback()
                }
            }
        }
    }

    // mettre à jour l'objet plant dans la BDD
    fun updatePlant(plant: PlantModel) {
        databaseRef.child(plant.id).setValue(plant)
    }

    // inserer une nouvelle plante en base de donnees
    fun insertPlant(plant: PlantModel) {
        databaseRef.child(plant.id).setValue(plant)
    }

    // supprimer la plante de la BDD
    fun deletePlant(plant: PlantModel) = databaseRef.child(plant.id).removeValue()

}