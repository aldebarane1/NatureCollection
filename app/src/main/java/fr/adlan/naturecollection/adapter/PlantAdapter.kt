package fr.adlan.naturecollection.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.adlan.naturecollection.*

class PlantAdapter(
    val context: MainActivity,
    private val plantList: List<PlantModel>,
    private val layoutId: Int
) : RecyclerView.Adapter<PlantAdapter.ViewHolder>() {

    // boite pour controler tout les composants à modifier : nom, image...

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // image de la plante
        val plantImage = view.findViewById<ImageView>(R.id.image_item)

        // info plante : nom+description
        val plantName: TextView? = view.findViewById(R.id.name_item)
        val plantDescription: TextView? = view.findViewById(R.id.description_item)
        val starIcon = view.findViewById<ImageView>(R.id.star_icon)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // récupérer les informations de la plante courante
        val currentPlant = plantList[position]


        // récupérer le repository
        val repo = PlantRepository()
        // utiliser glide pour recupérer l'image à partir de son lien et l'ajouter au composant
        Glide.with(context).load(Uri.parse(currentPlant.imageUrl)).into(holder.plantImage)

        // mise à jour info plante : nom+description
        holder.plantName?.text = currentPlant.name
        holder.plantDescription?.text = currentPlant.descriptor

        //vérifier si la plante a été liké
        if (currentPlant.liked) {
            holder.starIcon.setImageResource(R.drawable.ic_star)
        } else {
            holder.starIcon.setImageResource(R.drawable.ic_unstar)
        }
        // rajouter interaction du like
        holder.starIcon.setOnClickListener {
            // inverser le bouton si on appuie
            currentPlant.liked = !currentPlant.liked
            // mettre à jour l'objet plant
            repo.updatePlant(currentPlant)
        }
        // rajouter interaction clic sur une plante
        holder.itemView.setOnClickListener {
            // afficher la popup
            PlantPopup(this, currentPlant).show()
        }
    }

    override fun getItemCount(): Int {

        return plantList.size
    }

}