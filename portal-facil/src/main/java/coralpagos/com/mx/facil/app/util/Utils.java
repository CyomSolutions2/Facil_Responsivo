package coralpagos.com.mx.facil.app.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class Utils {
	
	public static String getValoresByDataOrder(JSONObject jsonObject, int dataOrder) {
        for (String key : jsonObject.keySet()) {
            JSONObject item = jsonObject.getJSONObject(key);
            if (item.getInt("dataOrder") == dataOrder) {
                JSONArray seleccionados = item.getJSONArray("seleccionados");
                List<String> valores = new ArrayList<>();
                for (int i = 0; i < seleccionados.length(); i++) {
                    JSONObject seleccionado = seleccionados.getJSONObject(i);
                    valores.add(seleccionado.getString("id"));
                }
                return String.join(", ", valores);
            }
        }
        return null; 
    }

}
