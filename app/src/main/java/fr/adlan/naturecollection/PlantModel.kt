package fr.adlan.naturecollection

class PlantModel(
        val id:String="plant0",
        val name: String = "Tulipe",
        val descriptor: String = "Petite description",
        val imageUrl: String = "http://graven.yt/plante.jpg",
        val grow:String="Faible",
        val water:String="Faible",
        var liked: Boolean = false
)