package br.com.speedy.wsipapp.util;

import org.json.JSONObject;

public class FiltroUtil {
	
	public static Filtro getFiltroPeixesDisponivel(String dados){
		JSONObject object = new JSONObject(dados);
		Filtro filtro = new Filtro();
		
		filtro.setDescricao(object.has("descricao") && object.getString("descricao") != null ? object.getString("descricao") : null);
		filtro.setCodigo(object.has("codigo") && object.getString("codigo") != null ? object.getString("codigo") : null);
		
		return filtro;
	}
	
	public static Filtro getArmazenamentos(String dados){
		JSONObject object = new JSONObject(dados);
		Filtro filtro = new Filtro();
		
		filtro.setDescricao(object.has("descricao") && object.getString("descricao") != null ? object.getString("descricao") : null);
		filtro.setCamara(object.has("camara") && object.getString("camara") != null ? object.getString("camara") : null);
		filtro.setCurral(object.has("curral") && object.getString("curral") != null ? object.getString("curral") : null);
		
		return filtro;
	}

	public static Filtro getFiltroPeixe(String dados){
		JSONObject object = new JSONObject(dados);
		Filtro filtro = new Filtro();

		filtro.setDescricao(object.has("descricao") && object.getString("descricao") != null ? object.getString("descricao") : null);

		return filtro;
	}

	public static Filtro getFiltroFornecedor(String dados){
		JSONObject object = new JSONObject(dados);
		Filtro filtro = new Filtro();

		filtro.setNome(object.has("nome") && object.getString("nome") != null ? object.getString("nome") : null);

		return filtro;
	}

	public static Filtro getFiltroId(String dados){
		JSONObject object = new JSONObject(dados);
		Filtro filtro = new Filtro();

		filtro.setId(object.has("id") && object.getLong("id") != 0 ? object.getLong("id") : null);

		return filtro;
	}

	public static Filtro getFiltroData(String dados){
		JSONObject object = new JSONObject(dados);
		Filtro filtro = new Filtro();

		filtro.setData(object.has("data") && !object.getString("data").isEmpty() ? object.getString("data") : null);

		return filtro;
	}

	public static Filtro getFiltroStatus(String dados){
		JSONObject object = new JSONObject(dados);
		Filtro filtro = new Filtro();

		filtro.setStatus(object.has("status") && !object.getString("status").isEmpty() ? object.getString("status") : null);

		return filtro;
	}

	public static Filtro getFiltroEstoque(String dados){
		JSONObject object = new JSONObject(dados);
		Filtro filtro = new Filtro();

		filtro.setId(object.has("id") && object.getLong("id") != 0 ? object.getLong("id") : null);
		filtro.setDescricao(object.has("descricao") && !object.getString("descricao").isEmpty()? object.getString("descricao") : null);
		filtro.setDataInicial(object.has("dataInicial") && !object.getString("dataInicial").isEmpty() ? object.getString("dataInicial") : null);
		filtro.setDataFinal(object.has("dataFinal") && !object.getString("dataFinal").isEmpty()? object.getString("dataFinal") : null);

		return filtro;
	}

}
