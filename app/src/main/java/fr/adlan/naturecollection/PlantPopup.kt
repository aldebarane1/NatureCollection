package fr.adlan.naturecollection

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import fr.adlan.naturecollection.adapter.PlantAdapter

class PlantPopup(
    private val adapter: PlantAdapter,
    private val currentPlant: PlantModel
) : Dialog(adapter.context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_plant_details)
        setupComponents()
        setupCloseButton()
        setupDeleteButton()
        setupStarButton()
    }
    private fun updateStar(button: ImageView) {
        // recupérer btn

        if (currentPlant.liked) {
            button.setImageResource(R.drawable.ic_star)
        } else {
            button.setImageResource(R.drawable.ic_unstar)
        }}

    private fun setupStarButton() {
        // recupérer btn
        val starButton = findViewById<ImageView>(R.id.star_button)
        updateStar(starButton)
        // interaction
        starButton.setOnClickListener {
            currentPlant.liked = !currentPlant.liked
            val repo = PlantRepository()
            repo.updatePlant(currentPlant)
            updateStar(starButton)
        }
    }

    private fun setupDeleteButton() {
        findViewById<ImageView>(R.id.delete_button).setOnClickListener {
            // supprimer la plante de la BDD
            val repo = PlantRepository()
            repo.deletePlant(currentPlant)
        }
    }

    private fun setupCloseButton() {
        findViewById<ImageView>(R.id.close_button).setOnClickListener {
            // fermer la fenêtre
            dismiss()
        }
    }

    private fun setupComponents() {
        // actualiser image plante
        val plantImage = findViewById<ImageView>(R.id.image_item)
        Glide.with(adapter.context).load(Uri.parse(currentPlant.imageUrl)).into(plantImage)
        // actualiser nom plante
        findViewById<TextView>(R.id.popup_plant_name).text = currentPlant.name
        // actualiser description plante
        findViewById<TextView>(R.id.popup_plant_descriptione_subtitle).text =
            currentPlant.descriptor
        // actualiser croissance plante
        findViewById<TextView>(R.id.popup_plant_grow_subtitle).text = currentPlant.grow
        // actualiser consommation eau plante
        findViewById<TextView>(R.id.popup_plant_water_subtitle).text = currentPlant.water
    }
}