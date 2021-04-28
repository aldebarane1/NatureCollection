package fr.adlan.naturecollection.fragments

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import fr.adlan.naturecollection.MainActivity
import fr.adlan.naturecollection.PlantModel
import fr.adlan.naturecollection.PlantRepository
import fr.adlan.naturecollection.PlantRepository.Singleton.downloadUri
import fr.adlan.naturecollection.R
import java.util.*

class AddPlantFragment(
    private val context: MainActivity
) : Fragment() {
    private var file: Uri? = null
    private var uploadedImage: ImageView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_plant, container, false)

        // recuperer image pour lui associer son composant
        uploadedImage = view.findViewById(R.id.preview_image)

        // recuperer bouton charger image
        val pickupImageButton = view.findViewById<Button>(R.id.uppload_button)

        // au clic on ouvre les images du telephone
        pickupImageButton.setOnClickListener { pickupImage() }

        // recuperer bouton confirmer
        val confirmButton = view.findViewById<Button>(R.id.confirm_button)
        confirmButton.setOnClickListener { sendForm(view) }
        Log.d(TAG, "onCreateView: FORMULAIRE")
        return view
    }

    private fun sendForm(view: View) {
        val repo = PlantRepository()
        repo.uploadImage(file!!) {
            val plantName = view.findViewById<EditText>(R.id.name_input).text.toString()
            val plantDescription =
                view.findViewById<EditText>(R.id.description_input).text.toString()
            val grow = view.findViewById<Spinner>(R.id.grow_spinner).selectedItem.toString()
            val water = view.findViewById<Spinner>(R.id.water_spinner).selectedItem.toString()
            val downloadImageUrl = downloadUri
            // LOG TEST
            Log.d("lol", "sendForm: name" + plantName)
            Log.d("lol", "sendForm: name" + grow)

            // creer un nouvel objet PlantModel
            val plant = PlantModel(
                UUID.randomUUID().toString(),
                plantName,
                plantDescription,
                downloadImageUrl.toString(),
                grow, water
            )
            // LOG TEST
            Log.d("lol", "sendForm: name" + plantName)
            Log.d("lol", "sendForm: name" + grow)

            // envoyer en BDD
            repo.insertPlant(plant)


        }

    }



    private fun pickupImage() {
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select picture"), 47)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 47 && resultCode == Activity.RESULT_OK) {

            // verifier si les donnees sont nulles on return
            if (data == null || data.data == null) return

            // si non nulles on récupère image
            file = data.data

            // mettre à jour apperçu image
            uploadedImage?.setImageURI(file)

//           //  heberger sur le bucket
//            val repo = PlantRepository()
//            repo.uploadImage(file!!)


        }
    }
}