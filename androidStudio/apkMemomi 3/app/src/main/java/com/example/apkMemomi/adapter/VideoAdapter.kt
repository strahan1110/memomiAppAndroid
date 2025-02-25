import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.example.apkMemomi.R
import com.example.apkMemomi.objetos.Videos
import com.example.apkMemomi.objetos.apiClients

class VideoAdapter(private val videoList: List<Videos>) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    private var currentlyPlayingVideo: VideoView? = null

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoView: VideoView = itemView.findViewById(R.id.videoView)
        val videoTitle: TextView = itemView.findViewById(R.id.videoTitle)
        val btnPlay: Button = itemView.findViewById(R.id.btnPlay)
        val btnPause: Button = itemView.findViewById(R.id.btnPause)
        val videoThumbnail: ImageView = itemView.findViewById(R.id.videoThumbnail) // Miniatura
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videoList[position]
        val videoUri = Uri.parse("${apiClients.BASE_URL_VIDEO}${video.VideoFisic}")

        holder.videoTitle.text = video.Videos

        loadThumbnail(videoUri, holder.videoThumbnail)

        holder.videoView.setVideoURI(videoUri)


        holder.btnPlay.setOnClickListener {

            currentlyPlayingVideo?.pause()
            currentlyPlayingVideo = holder.videoView

            // Reproducir el video y ocultar la miniatura
            holder.videoThumbnail.visibility = View.GONE
            holder.videoView.visibility = View.VISIBLE
            holder.videoView.setVideoURI(videoUri)
            holder.videoView.start()
        }

        holder.btnPause.setOnClickListener {
            holder.videoView.pause() // Pausa el video actual
        }

        holder.videoView.setOnCompletionListener {
            //currentlyPlayingVideo = null // Limpia el video en reproducción al finalizar

            // Al terminar de reproducir, ocultar el VideoView y mostrar la miniatura nuevamente
            holder.videoThumbnail.visibility = View.VISIBLE
            holder.videoView.visibility = View.GONE
            currentlyPlayingVideo = null
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }


    private fun loadThumbnail(videoUri: Uri, imageView: ImageView) {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(videoUri.toString())
        val bitmap: Bitmap = retriever.getFrameAtTime(0) ?: return // Obtener el primer fotograma
        imageView.setImageBitmap(bitmap)
    }


    override fun getItemCount(): Int = videoList.size
}
