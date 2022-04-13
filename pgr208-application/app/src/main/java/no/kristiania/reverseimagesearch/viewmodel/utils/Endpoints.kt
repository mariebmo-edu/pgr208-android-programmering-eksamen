package no.kristiania.reverseimagesearch.viewmodel.utils

object Endpoints {
    private const val base_url = "http://api-edu.gtl.ai/api/v1/imagesearch/"

    //POST
    const val upload_url = base_url + "upload"

    //GET
    const val get_google_url = base_url + "google"
    const val get_bing_url = base_url + "bing"
    const val get_tinyEye_url = base_url + "tinyeye"
}