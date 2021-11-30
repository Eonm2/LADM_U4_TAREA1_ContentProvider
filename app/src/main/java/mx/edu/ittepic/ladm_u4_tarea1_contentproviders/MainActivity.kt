package mx.edu.ittepic.ladm_u4_tarea1_contentproviders

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CallLog
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.concurrent.ThreadPoolExecutor

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        boton.setOnClickListener {
            listarCalendario()
        }

        boton1.setOnClickListener {
            listaLlamadasPerdidas()
        }


    }

    private fun listarCalendario(){
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CALENDAR)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.READ_CALENDAR),1)

        }else{
            var calendarios = ArrayList<String>()
            var projecion = arrayOf(CalendarContract.Events.TITLE)
            var cursorCalendario : Cursor?=null

            try{
                cursorCalendario = contentResolver.query(CalendarContract.Events.CONTENT_URI,projecion,null,null,null)

                while(cursorCalendario?.moveToNext()!!){
                    calendarios.add(cursorCalendario.getString(0))
                }

            }catch (ex:Exception){
                Toast.makeText(this,"Error: "+ex,Toast.LENGTH_LONG).show()
            }
            finally {
                listaCal.adapter = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,calendarios)
                cursorCalendario?.close()
            }
        }

    }

    @SuppressLint("Range")
    private fun listaLlamadasPerdidas(){
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.READ_CALL_LOG),1)
        }else{
            var llamadas =  ArrayList<String>()
            var selection:String = CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE
            var cursor:Cursor?=null

            try {
                cursor = contentResolver.query(Uri.parse("content://call_log/calls"),null,selection,null,null)
                var registros =""

                while(cursor?.moveToNext()!!){
                    val numero:String = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
                    val localizacion:String = cursor.getString(cursor.getColumnIndex(CallLog.Calls.GEOCODED_LOCATION))

                    registros = "\nNumero: "+numero+"\nLocalizacion: "+ localizacion+"\n"

                    llamadas.add(registros)
                }
            }catch (ex:Exception){
                Toast.makeText(this,"Error: "+ex,Toast.LENGTH_LONG).show()
            }
            finally {
                listalLamadas.adapter = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,llamadas)
                cursor?.close()
            }
        }
    }

}

