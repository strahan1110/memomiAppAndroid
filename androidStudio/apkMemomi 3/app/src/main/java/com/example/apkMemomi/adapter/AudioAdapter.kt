import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import com.example.apkMemomi.R
import com.example.apkMemomi.objetos.Audios
import com.example.apkMemomi.objetos.apiClients




class AudioAdapter(
    private val context: Context,
    private val audioList: List<Audios>
) : RecyclerView.Adapter<AudioAdapter.AudioViewHolder>() {

    private var exoPlayer: ExoPlayer? = null


    inner class AudioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAudioName: TextView = itemView.findViewById(R.id.tvAudioName)
        val tvAudioDescription: TextView = itemView.findViewById(R.id.tvAudioDescription)
        val btnPlayAudio: Button = itemView.findViewById(R.id.btnPlayAudio)
        val btnStopAudio: Button = itemView.findViewById(R.id.btnStopAudio)

        fun bind(audio : Audios){
            tvAudioName.text = audio.Audio
            tvAudioDescription.text = audio.IdEvento

            btnPlayAudio.setOnClickListener {
                playAudio("${apiClients.BASE_URL_AUDIO}${audio.AudioFisc}")
                tvAudioName.text = "${apiClients.BASE_URL_AUDIO}${audio.AudioFisc}"
            }
            btnStopAudio.setOnClickListener {
                stopAudio()
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_audio, parent, false)
        return AudioViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        val audio = audioList[position]

        holder.bind(audio)
    }

    override fun getItemCount(): Int = audioList.size



    private fun playAudio(url: String) {

        exoPlayer?.release() // Libera el reproductor previo si está en uso
        exoPlayer = ExoPlayer.Builder(context).build()
        println("Reproducir audio url: $url")
        val mediaItem = MediaItem.fromUri(url)
        exoPlayer?.setMediaItem(mediaItem)
        exoPlayer?.prepare()
        exoPlayer?.play()
    }
    fun releasePlayer() {
        exoPlayer?.release()
        exoPlayer = null
    }

    private fun stopAudio() {
        exoPlayer?.stop()
        exoPlayer?.release()
        exoPlayer = null
    }


}
