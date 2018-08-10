package br.com.speedy.wsipapp;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import br.com.speedy.wsipapp.enumerated.Posto;
import br.com.speedy.wsipapp.enumerated.StatusArmazenamento;
import br.com.speedy.wsipapp.enumerated.StatusPedido;
import br.com.speedy.wsipapp.model.*;
import br.com.speedy.wsipapp.repository.*;
import br.com.speedy.wsipapp.util.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jboss.logging.annotations.Pos;
import org.jboss.weld.bean.builtin.ee.ServletContextBean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.speedy.wsipapp.enumerated.StatusCompra;
import br.com.speedy.wsipapp.enumerated.TipoDestinoRetirada;


@Path("/myresource")
public class MyResource {
	
	private Compra compra;
	
	private Observacao observacao;

	private Impressao impressao;

	//Utilizar em desenvolvimento
	//private String urlApp = "http://192.168.56.1:8080/arquivos/";
	//Utilizar em producao
	//private String urlApp = "http://192.168.1.13:8080/ws_ipapp/arquivos/";
	//Utilizar em producao no iranduba
	private String urlApp = "http://192.168.110.2:7777/ws_ipapp/arquivos/";

	@Context
	private ServletContext servletContext;


	public MyResource() {
		// TODO Auto-generated constructor stub
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/logar")
	public String logar(String data) {

		JSONObject jUsuario = new JSONObject();

		EntityManager manager = JpaUtil.getEntityManager();

		try{

		JSONObject object = new JSONObject(data);
		
		String login = object.getString("login");
		String senha = object.getString("senha");
		String imei = "";//object.getString("imei");
		

		UsuarioRepository usuarios = new UsuarioRepository(manager);

		Usuario usuario = usuarios.getUsuario(login, senha, imei);
		

		jUsuario.put("valido", usuario == null ? false : true);
		if(usuario != null){
			jUsuario.put("id", usuario.getId());
			jUsuario.put("nome", usuario.getNome());

			UltimoLoginRepository loginRepository = new UltimoLoginRepository(manager);
			UltimoLogin ul = loginRepository.getUltimoLoginUsuario(usuario);
			JSONObject jUL = new JSONObject();
			jUL.put("valido", ul == null ? false : true);
			if (ul != null) {
				ul.setData(new Date());
				jUL.put("id", ul.getId());
				jUL.put("data", new SimpleDateFormat("hh:mm:ss dd/MM/yyyy").format(ul.getData()));
				loginRepository.salvarUltimoLogin(ul);
			}
			jUsuario.put("ultimologin", jUL);
		}

		}catch (Exception e){
			e.printStackTrace();
		}finally {
			manager.close();
		}

		return jUsuario.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/salvarUsuario")
	public String salvarUsuario(String dados){

		JSONObject retorno = new JSONObject();

		EntityManager manager = JpaUtil.getEntityManager();

		try {
			JSONObject object = new JSONObject(dados);

			Usuario usuario = new Usuario();
			usuario.setId(object.getLong("id"));
			String senha = object.getString("senha");

			UsuarioRepository uRepo = new UsuarioRepository(manager);

			usuario = uRepo.getUsuarioPorId(usuario);

			usuario.setSenha(senha);

			uRepo.salvarUsuario(usuario);

			UltimoLoginRepository loginRepository = new UltimoLoginRepository(manager);
			UltimoLogin ultimoLogin = loginRepository.getUltimoLoginUsuario(usuario);
			if (ultimoLogin == null)
				ultimoLogin = new UltimoLogin();
			ultimoLogin.setData(new Date());
			ultimoLogin.setUsuario(usuario);

			loginRepository.salvarUltimoLogin(ultimoLogin);

			retorno.put("valido", true);
		}catch (Exception e){
			e.printStackTrace();
			retorno.put("valido", false);
		}finally {
			manager.close();
		}

		return retorno.toString();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/salvarCompra")
	public Boolean salvarCompra(String data) {

		EntityManager manager = JpaUtil.getEntityManager();

		try {
			JSONObject object = new JSONObject(data);
			
			String posto = object.getString("posto");
			
			compra = new Compra();
			compra = getCompra(object);	

			CompraRepository compras = new CompraRepository(manager);
			
			Rastreabilidade rastreabilidade = new Rastreabilidade();
			rastreabilidade.setPosto(posto);
			rastreabilidade.setCompra(compra);
			compras.salvarCompra(compra, observacao, impressao, rastreabilidade);
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			manager.close();
		}
		
		return true;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/descartarCompra")
	public Boolean descartarCompra(String data) {

		EntityManager manager = JpaUtil.getEntityManager();

		try {
			JSONObject object = new JSONObject(data);
			compra = new Compra();

			if (object.has("id"))
				compra.setId(object.getLong("id"));

			CompraRepository compras = new CompraRepository(manager);
			LoteRepository lr = new LoteRepository(manager);

			compra = compras.getCompraPorId(compra);
			compra.setPause(true);
			compra.setStatus(false);
			compra.setStatusCompra(StatusCompra.DESCARTADA);
			//compra.setLotes(lr.getLotesPorCompra(compra));

			compras.descartarCompra(compra);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			manager.close();
		}

		return true;
	}

	public Compra getCompra(JSONObject object){

		BigDecimal valorTotalCompra = BigDecimal.ZERO;

		EntityManager manager = JpaUtil.getEntityManager();
		PrecoDiferenciadoRepository repository = new PrecoDiferenciadoRepository(manager);

		compra = new Compra();
		
		String posto = object.getString("posto");

		if (object.has("id"))
			compra.setId(object.getLong("id"));

		compra.setDataCompra(new Date());
		compra.setAutorizado(null);
		compra.setCodigo(object.getString("codigo"));
		compra.setPause(object.getBoolean("pause"));
		compra.setStatus(object.getBoolean("status"));
		compra.setLotes(new ArrayList<Lote>());
		compra.setValorTotal(new BigDecimal(object.getDouble("valorTotal")));
		if (compra.getPause() != null && compra.getPause())
			compra.setStatusCompra(StatusCompra.SALVA);
		else
			compra.setStatusCompra(StatusCompra.ENVIADO);
		compra.setIdUsuarioCompra(object.getLong("idUsuarioCompra"));
		
		if (!posto.equals(Posto.TUNEL.toString())){
			JSONObject jsonFornec = object.getJSONObject("fornecedor");
			JSONObject jsonBarco = object.getJSONObject("barco");
			
			Fornecedor fornecedor = new Fornecedor();
			fornecedor.setId(jsonFornec.getLong("id"));
			fornecedor.setNome(jsonFornec.getString("nome"));
			
			Barco barco = new Barco();
			barco.setId(jsonBarco.getLong("id"));
			barco.setNome(jsonBarco.getString("nome"));
			
			compra.setFornecedor(fornecedor);
			compra.setBarco(barco);
		}
		
		JSONArray jsonArray = object.getJSONArray("lotes");
		
		for (int i = 0, tam = jsonArray.length(); i < tam ; i++) {
			
			Lote lote = new Lote();
			Peixe p = new Peixe();
			
			JSONObject loteJson =  jsonArray.getJSONObject(i);
			JSONObject peixeJson =  loteJson.getJSONObject("peixe");
			p.setId(peixeJson.getLong("id"));
			p.setDescricao(peixeJson.getString("descricao"));
			
			if (loteJson.has("id"))
				lote.setId(loteJson.getLong("id"));

			lote.setPeixe(p);
			lote.setCompra(new Compra());
			lote.setCompra(compra);
			lote.setFornecedor(new Fornecedor());
			lote.setFornecedor(compra.getFornecedor());
			lote.setPeso(new BigDecimal(loteJson.getDouble("peso")));
			lote.setQtdCaixas(loteJson.getInt("qtdCaixas"));
			lote.setPesoCacapa(new BigDecimal(loteJson.getDouble("pesoCacapa")));

			PrecoDiferenciado pd = null;
			
			if (lote.getFornecedor() != null)
				pd = repository.getPrecoDiferenciadoPorFornecedorEPeixe(lote.getFornecedor(), lote.getPeixe());

			if (pd != null){
				lote.setValorUnitarioPeixe(pd.getValor());
				lote.setValor(lote.getPesoLiquido().multiply(pd.getValor()));
				lote.setIsPrecoDiferenciado(true);
			}else {
				lote.setValor(new BigDecimal(loteJson.getDouble("valor")));
				lote.setValorUnitarioPeixe(new BigDecimal(loteJson.getDouble("valorUnitarioPeixe")));
				lote.setIsPrecoDiferenciado(false);
			}

			//lote.setSequencia(loteJson.getInt("sequencia"));
			lote.setDescontokg(new BigDecimal(loteJson.getDouble("descontokg")));

			lote.setAcrescimo(new BigDecimal(loteJson.getDouble("acrescimo")));
			lote.setDesconto(new BigDecimal(loteJson.getDouble("desconto")));

			valorTotalCompra = valorTotalCompra.add(lote.getValor());
			
			compra.getLotes().add(lote);
		}

		compra.setValorTotal(valorTotalCompra);
		
		if (!compra.getPause() && object.getString("observacao") != null && !object.getString("observacao").isEmpty()){
			observacao = new Observacao();
			observacao.setData(new Date());
			observacao.setCompra(compra);
			observacao.setObservacao(object.getString("observacao"));
			Usuario usu = new Usuario();
			usu.setId(object.getLong("idUsuarioCompra"));
			observacao.setUsuario(usu);
			compra.setObservacao(null);
		}else{
			compra.setObservacao(object.getString("observacao"));
			observacao = null;
		}

		//impressao = new Impressao();
		//impressao.setConteudo(getConteudoImpressao(compra, object.getString("nomeUsuarioCompra")));
		
		return compra;
	}

	public String getConteudoImpressao(Compra compra, String usuario){
		//char[] cutP = new char[]{0x1d, 'V', 1};
		BigDecimal totalPesoLiquido = BigDecimal.ZERO;
		BigDecimal totalPesoBruto = BigDecimal.ZERO;
		Integer totalCaixas = 0;
		BigDecimal totalDescontos = BigDecimal.ZERO;;
		String conteudo = "  IRANDUBA PESCADOS \n\n";
		conteudo += "COMPROVANTE DE COMPRA \n\n";
		conteudo += "DATA: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(compra.getDataCompra()) + " \n";
		conteudo += "CODIGO: " + compra.getCodigo() + " \n";
		conteudo += "USUARIO: " + usuario + " \n";
		conteudo += "FORNECEDOR: " + compra.getFornecedor().getNome() + " \n";
		conteudo += "TRANSPORTE: " + compra.getBarco().getNome() + " \n";
		conteudo += "ITENS: (" + compra.getLotes().size() + ") \n";
		conteudo += "----------------------------------------\n";

		for (Lote lote : compra.getLotes()) {
			BigDecimal pesoLiquido = new BigDecimal(String.valueOf(lote.getPeso().subtract(lote.getPesoCacapa().multiply(new BigDecimal(lote.getQtdCaixas())))));
			conteudo += lote.getPeixe().getDescricao() + "  " + lote.getPeso().toString() + "KG - " + lote.getQtdCaixas().toString() + " = " + pesoLiquido.toString() + "KG\n";

			totalPesoBruto = totalPesoBruto.add(lote.getPeso());
			totalPesoLiquido = totalPesoLiquido.add(pesoLiquido);
			totalDescontos = totalDescontos.add(lote.getDescontokg());
			totalCaixas = totalCaixas + lote.getQtdCaixas();
		}

		conteudo += "\nTOTAIS----------------------------------\n";
		conteudo += "TOTAL P. BRUTO: " + totalPesoBruto.toString() + "KG \n";
		conteudo += "TOTAL P. LIQUIDO: " + totalPesoLiquido.toString() + "KG \n";
		conteudo += "TOTAL DESCONTOS: " + totalDescontos.toString() + "KG \n";
		conteudo += "TOTAL CAIXAS: " + totalCaixas.toString() + " \n";

		conteudo +=	"----------------------------------------";

		//conteudo += "\n \n \n \n \n \n" + new String(cutP);

		return conteudo;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/salvarImpressao")
	public Boolean salvarImpressao(String data) {

		EntityManager manager = JpaUtil.getEntityManager();

		try {
			JSONObject object = new JSONObject(data);

			Impressao impressao1 = new Impressao();
			impressao1.setConteudo(object.getString("conteudo"));

			ImpressaoRepository imp = new ImpressaoRepository(manager);
			imp.salvarImpressao(impressao1);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			manager.close();
		}

		return true;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getComprasSalvas")
	public List<Compra> getComprasSalvas() {
		
		EntityManager manager = JpaUtil.getEntityManager();
		List<Compra> lista = new ArrayList<Compra>();

		try{
			CompraRepository compraR = new CompraRepository(manager);
			lista = compraR.getComprasSalvas();
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			manager.close();
		}

		return lista;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getLotesCompra")
	public List<Lote> getLotesCompra(String idCompra) {
		
		EntityManager manager = JpaUtil.getEntityManager();
		List<Lote> lista = new ArrayList<Lote>();

		try{
			LoteRepository lotes = new LoteRepository(manager);
			lista = lotes.getLotesCompra(idCompra);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			manager.close();
		}
		
		return lista;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getConfiguracoes")
	public Configuracoes getConfiguracoes(){

		EntityManager manager = JpaUtil.getEntityManager();
		List<Configuracoes> list = new ArrayList<Configuracoes>();

		try{
			ConfiguracoesRepository repository = new ConfiguracoesRepository(manager);
			list = repository.getConfiguracoes();
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			manager.close();
		}

		return list.size() > 0 ? list.get(0) : null;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getPeixesFiltro")
	public List<Peixe> getPeixesFiltro(String dados){

		EntityManager manager = JpaUtil.getEntityManager();
		List<Peixe> lista = new ArrayList<Peixe>();

		try{
			PeixeRepository peixes = new PeixeRepository(manager);
			Filtro filtro = FiltroUtil.getFiltroPeixe(dados);
			lista = peixes.getPeixesFiltro(filtro);

			for (Peixe peixe : lista){
				peixe.setUrlFoto(getUrlFoto(manager, peixe));
			}

		}catch (Exception e){
			e.printStackTrace();
		}finally {
			manager.close();
		}

		return lista;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getPeixesVendaFiltro")
	public List<Peixe> getPeixesVendaFiltro(String dados){

		EntityManager manager = JpaUtil.getEntityManager();
		List<Peixe> lista = new ArrayList<Peixe>();

		try{
			PeixeRepository peixes = new PeixeRepository(manager);
			Filtro filtro = FiltroUtil.getFiltroPeixe(dados);
			lista = peixes.getPeixesVendaFiltro(filtro);

			for (Peixe peixe : lista){
				peixe.setUrlFoto(getUrlFoto(manager, peixe));
			}

		}catch (Exception e){
			e.printStackTrace();
		}finally {
			manager.close();
		}

		return lista;
	}

	public String getUrlFoto(EntityManager manager, Peixe peixe){

		InputStream inputStream = null;
		OutputStream outputStream = null;

		String retorno = "";
		try{

			//ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			//HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			//String path = request.getSession().getServletContext().getRealPath( "/" );

			String path = servletContext.getRealPath("/");

			System.out.println(path);

			ArquivoRepository arquivoRepository = new ArquivoRepository(manager);
			List<Arquivo> arquivos = arquivoRepository.getArquivoPorPeixe(peixe);

			if(arquivos != null && arquivos.size() > 0){
				Arquivo arquivo = arquivos.get(0);

				File foto = new File("C:" + File.separator + "Iranduba Pescados" + File.separator + "arquivos" + File.separator + "imagens" + File.separator + arquivo.getNome());
				File destino = new File(path + "arquivos/" + arquivo.getNome());

				if (foto.exists()) {
					inputStream = new FileInputStream(foto);
					outputStream = new FileOutputStream(destino);

					byte[] buffer = new byte[1024];

					int length;
					while ((length = inputStream.read(buffer)) > 0) {
						outputStream.write(buffer, 0, length);
					}

					retorno = urlApp + arquivo.getNome();
				}
				else
					retorno = "";
			}

		}catch (Exception e){
			e.printStackTrace();
			retorno = "";
		}finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}

		return retorno;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getFornecedoresFiltro")
	public List<Fornecedor> getFornecedoresFiltro(String dados){

		EntityManager manager = JpaUtil.getEntityManager();
		List<Fornecedor> lista = new ArrayList<Fornecedor>();

		try{
			FornecedoresRepository fornecedores = new FornecedoresRepository(manager);
			BarcoRepository barcoRepository = new BarcoRepository(manager);
			Filtro filtro = FiltroUtil.getFiltroFornecedor(dados);
			lista = fornecedores.getFornecedoresFiltro(filtro);

			for (Fornecedor fornecedor : lista) {
				fornecedor.setBarcos(barcoRepository.getBarcosPorFornecedor(fornecedor));
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			manager.close();
		}

		return lista;
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getCamarasETipos")
	public String getCamarasETipos(){
		EntityManager manager = JpaUtil.getEntityManager();
		JSONObject jRetorno = new JSONObject();

		try{

			TipoPeixeRepository repository = new TipoPeixeRepository(manager);
			List<TipoPeixe> tipos = repository.getTiposPeixe();

			CamaraRepository camaraRepository = new CamaraRepository(manager);
			List<Camara> camaras = camaraRepository.getCamaras();

			JSONArray jCamaras = new JSONArray();

			for(Camara camara : camaras){
				JSONObject jCamara = new JSONObject();
				jCamara.put("id", camara.getId());
				jCamara.put("descricao", camara.getDescricao());

				jCamaras.put(jCamara);
			}

			JSONArray jTipos = new JSONArray();

			for(TipoPeixe tipoPeixe : tipos){
				JSONObject jTipo = new JSONObject();
				jTipo.put("id", tipoPeixe.getId());
				jTipo.put("descricao", tipoPeixe.getDescricao());

				jTipos.put(jTipo);
			}


			jRetorno.put("camaras", jCamaras);
			jRetorno.put("tipos", jTipos);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			manager.close();
		}

		return jRetorno.toString();
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getCamarasTiposETamanhos")
	public String getCamarasTiposETamanhos(){
		EntityManager manager = JpaUtil.getEntityManager();
		JSONObject jRetorno = new JSONObject();

		try{

			TipoPeixeRepository repository = new TipoPeixeRepository(manager);
			List<TipoPeixe> tipos = repository.getTiposPeixe();

			CamaraRepository camaraRepository = new CamaraRepository(manager);
			List<Camara> camaras = camaraRepository.getCamaras();

			TamanhoPeixeRepository tamRepository = new TamanhoPeixeRepository(manager);
			List<TamanhoPeixe> tamanhos = tamRepository.getTamanhos();

			JSONArray jCamaras = new JSONArray();

			for(Camara camara : camaras){
				JSONObject jCamara = new JSONObject();
				jCamara.put("id", camara.getId());
				jCamara.put("descricao", camara.getDescricao());

				jCamaras.put(jCamara);
			}

			JSONArray jTipos = new JSONArray();

			for(TipoPeixe tipoPeixe : tipos){
				JSONObject jTipo = new JSONObject();
				jTipo.put("id", tipoPeixe.getId());
				jTipo.put("descricao", tipoPeixe.getDescricao());

				jTipos.put(jTipo);
			}

			JSONArray jTamanhos = new JSONArray();

			for (TamanhoPeixe tamanho : tamanhos) {
				JSONObject jTamanho = new JSONObject();
				jTamanho.put("id", tamanho.getId());
				jTamanho.put("descricao", tamanho.getDescricao());

				jTamanhos.put(jTamanho);
			}

			jRetorno.put("camaras", jCamaras);
			jRetorno.put("tipos", jTipos);
			jRetorno.put("tamanhos", jTamanhos);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			manager.close();
		}

		return jRetorno.toString();
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getCamaras")
	public String getCamaras(){
		EntityManager manager = JpaUtil.getEntityManager();
		JSONObject jRetorno = new JSONObject();

		try{
			CamaraRepository camaraRepository = new CamaraRepository(manager);
			List<Camara> camaras = camaraRepository.getCamaras();

			JSONArray jCamaras = new JSONArray();

			for(Camara camara : camaras){
				JSONObject jCamara = new JSONObject();
				jCamara.put("id", camara.getId());
				jCamara.put("descricao", camara.getDescricao());

				jCamaras.put(jCamara);
			}

			jRetorno.put("camaras", jCamaras);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			manager.close();
		}

		return jRetorno.toString();
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getTipos")
	public String getTipos(){
		EntityManager manager = JpaUtil.getEntityManager();
		JSONObject jRetorno = new JSONObject();

		try{
			TipoPeixeRepository repository = new TipoPeixeRepository(manager);
			List<TipoPeixe> tipos = repository.getTiposPeixe();

			JSONArray jTipos = new JSONArray();

			for(TipoPeixe tipoPeixe : tipos){
				JSONObject jTipo = new JSONObject();
				jTipo.put("id", tipoPeixe.getId());
				jTipo.put("descricao", tipoPeixe.getDescricao());

				jTipos.put(jTipo);
			}

			jRetorno.put("tipos", jTipos);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			manager.close();
		}

		return jRetorno.toString();
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getTransportadoras")
	public String getTransportadoras(){
		EntityManager manager = JpaUtil.getEntityManager();
		JSONObject jRetorno = new JSONObject();

		try{
			TransportadoraRepository repository = new TransportadoraRepository(manager);
			List<Transportadora> transportadoras = repository.getTransportadoras();

			JSONArray jTransportadoras = new JSONArray();

			for(Transportadora transportadora : transportadoras){
				JSONObject jTipo = new JSONObject();
				jTipo.put("id", transportadora.getId());
				jTipo.put("nome", transportadora.getNome());

				jTransportadoras.put(jTipo);
			}

			jRetorno.put("transportadoras", jTransportadoras);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			manager.close();
		}

		return jRetorno.toString();
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getDadosArmazenamento")
	public String getDadosArmazenamento(){

		EntityManager manager = JpaUtil.getEntityManager();
		JSONObject dadosArmazenamento = new JSONObject();

		try{

			CamaraRepository camaraRepository = new CamaraRepository(manager);
			EmbalagemRepository embalagemRepository = new EmbalagemRepository(manager);
			TamanhoPeixeRepository tamanhoPeixeRepository = new TamanhoPeixeRepository(manager);
			TipoPeixeRepository tipoPeixeRepository = new TipoPeixeRepository(manager);


			List<Camara> camaras = camaraRepository.getCamaras();
			List<Embalagem> embalagems = embalagemRepository.getEmbalagens();
			List<TamanhoPeixe> tamanhoPeixes = tamanhoPeixeRepository.getTamanhos();
			List<TipoPeixe> tipoPeixes = tipoPeixeRepository.getTiposPeixe();

			JSONArray jCamaras = new JSONArray();

			for(Camara camara : camaras){
				JSONObject jCamara = new JSONObject();
				jCamara.put("id", camara.getId());
				jCamara.put("descricao", camara.getDescricao());

				jCamaras.put(jCamara);
			}

			JSONArray jEmbalagens = new JSONArray();

			for(Embalagem embalagem : embalagems){
				JSONObject jEmbalagem = new JSONObject();
				jEmbalagem.put("id", embalagem.getId());
				jEmbalagem.put("descricao", embalagem.getDescricao());

				jEmbalagens.put(jEmbalagem);
			}

			JSONArray jTamanhos = new JSONArray();

			for(TamanhoPeixe tamanhoPeixe : tamanhoPeixes){
				JSONObject jTamanho = new JSONObject();
				jTamanho.put("id", tamanhoPeixe.getId());
				jTamanho.put("descricao", tamanhoPeixe.getDescricao());

				jTamanhos.put(jTamanho);
			}

			JSONArray jTipos = new JSONArray();

			for(TipoPeixe tipoPeixe : tipoPeixes){
				JSONObject jTipo = new JSONObject();
				jTipo.put("id", tipoPeixe.getId());
				jTipo.put("descricao", tipoPeixe.getDescricao());

				jTipos.put(jTipo);
			}

			dadosArmazenamento.put("camaras", jCamaras);
			dadosArmazenamento.put("embalagens", jEmbalagens);
			dadosArmazenamento.put("tamanhos", jTamanhos);
			dadosArmazenamento.put("tipos", jTipos);

			tipoPeixes = null;
			embalagems = null;
			tamanhoPeixes = null;
			camaras = null;

		}catch (Exception e){
			e.printStackTrace();
		}finally {
			manager.close();
		}

		return dadosArmazenamento.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getPosicoesCamara")
	public List<PosicaoCamara> getPosicoesCamara(String dados){

		EntityManager manager = JpaUtil.getEntityManager();
		List<PosicaoCamara> lista = new ArrayList<PosicaoCamara>();

		try{
			PosicaoCamaraRepository posicaoCamaraRepository = new PosicaoCamaraRepository(manager);
			Filtro filtro = FiltroUtil.getFiltroId(dados);
			lista = posicaoCamaraRepository.getPosicoesPorCamara(filtro);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			manager.close();
		}

		return lista;
	}

	/*@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/buscarArmazenamentosERetiradas")
	public String buscarArmazenamentosERetiradas() {

		EntityManager manager = JpaUtil.getEntityManager();
		JSONObject jRetorno = new JSONObject();
		JSONArray jLista = new JSONArray();

		try{
			CompraRepository compraR = new CompraRepository(manager);
			lista = compraR.getComprasSalvas();
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			manager.close();
		}

		return lista;
	}*/

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/salvarArmazenamentosERetiradas")
	public String salvarArmazenamentosERetiradas(String dados){

		EntityManager manager = JpaUtil.getEntityManager();
		JSONObject jRetorno = new JSONObject();

		try{

			JSONObject object = new JSONObject(dados);
			JSONArray jListas = object.getJSONArray("armazenamentosERetiradas");

			List<Armazenamento> armazenamentos = new ArrayList<Armazenamento>();
			List<Retirada> retiradas = new ArrayList<Retirada>();
			
			String posto = object.getString("posto");

			Date dataSalvo = new Date();

			for (int i = 0; i < jListas.length(); i++) {

				String tipo = jListas.getJSONObject(i).getString("tipo");

				if (tipo.equals("Armazenar")){

					Armazenamento armazenamento = new Armazenamento();
					armazenamento.setId(jListas.getJSONObject(i).has("id") ? jListas.getJSONObject(i).getLong("id") : null);
					armazenamento.setPeso(new BigDecimal(jListas.getJSONObject(i).getDouble("peso")));
					armazenamento.setPesoEmbalagem(new BigDecimal(jListas.getJSONObject(i).getDouble("pesoEmbalagem")));
					armazenamento.setQtdeEmbalagem(jListas.getJSONObject(i).getInt("qtdeEmbalagem"));
					armazenamento.setData(new Date());
					if (!posto.equals(Posto.TUNEL.toString())){
						if (jListas.getJSONObject(i).getString("status").equals("enviar"))
							armazenamento.setStatus(StatusArmazenamento.AUTORIZADO);
						else {
							armazenamento.setStatus(StatusArmazenamento.SALVO);
							armazenamento.setDataSalvo(dataSalvo);
						}
					}else{
						armazenamento.setStatus(StatusArmazenamento.TUNEL);
					}
						
					armazenamento.setObservacao(jListas.getJSONObject(i).has("observacao") ? jListas.getJSONObject(i).getString("observacao") : null);

					Usuario usuario = new Usuario();
					usuario.setId(jListas.getJSONObject(i).getJSONObject("usuario").getLong("id"));
					armazenamento.setUsuario(usuario);

					if (!posto.equals(Posto.TUNEL.toString())){
						Camara camara = new Camara();
						camara.setId(jListas.getJSONObject(i).getJSONObject("camara").getLong("id"));
						armazenamento.setCamara(camara);
						
						PosicaoCamara posicaoCamara = new PosicaoCamara();
						posicaoCamara.setId(jListas.getJSONObject(i).getJSONObject("posicaoCamara").getLong("id"));
						armazenamento.setPosicaoCamara(posicaoCamara);
					}

					if (jListas.getJSONObject(i).getJSONObject("embalagem").has("id")) {
						Embalagem embalagem = new Embalagem();
						embalagem.setId(jListas.getJSONObject(i).getJSONObject("embalagem").getLong("id"));
						armazenamento.setEmbalagem(embalagem);
					}else
						armazenamento.setEmbalagem(null);

					Peixe peixe = new Peixe();
					peixe.setId(jListas.getJSONObject(i).getJSONObject("peixe").getLong("id"));
					armazenamento.setPeixe(peixe);

					if (jListas.getJSONObject(i).getJSONObject("tamanhoPeixe").has("id")) {
						TamanhoPeixe tamanhoPeixe = new TamanhoPeixe();
						tamanhoPeixe.setId(jListas.getJSONObject(i).getJSONObject("tamanhoPeixe").getLong("id"));
						armazenamento.setTamanhoPeixe(tamanhoPeixe);
					}
					else
						armazenamento.setTamanhoPeixe(null);

					TipoPeixe tipoPeixe = new TipoPeixe();
					tipoPeixe.setId(jListas.getJSONObject(i).getJSONObject("tipoPeixe").getLong("id"));
					armazenamento.setTipoPeixe(tipoPeixe);

					armazenamentos.add(armazenamento);
				}else{
					Retirada retirada = new Retirada();
					retirada.setId(jListas.getJSONObject(i).has("id") ? jListas.getJSONObject(i).getLong("id") : null);
					retirada.setPeso(new BigDecimal(jListas.getJSONObject(i).getDouble("peso")));
					retirada.setQtdeEmbalagem(jListas.getJSONObject(i).getInt("qtdeEmbalagem"));
					retirada.setDestino(TipoDestinoRetirada.valueOf(jListas.getJSONObject(i).getString("destino")));
					if (!posto.equals(Posto.TUNEL.toString())){
						if (jListas.getJSONObject(i).getString("status").equals("enviar"))
							retirada.setStatus(StatusArmazenamento.AUTORIZADO);
						else{
							retirada.setStatus(StatusArmazenamento.SALVO);
							retirada.setDataSalvo(dataSalvo);
						}
					}else{
						retirada.setStatus(StatusArmazenamento.TUNEL);
					}
					retirada.setData(new Date());
					retirada.setObservacao(jListas.getJSONObject(i).has("observacao") ? jListas.getJSONObject(i).getString("observacao") : null);

					Usuario usuario = new Usuario();
					usuario.setId(jListas.getJSONObject(i).getJSONObject("usuario").getLong("id"));
					retirada.setUsuario(usuario);

					Peixe peixe = new Peixe();
					peixe.setId(jListas.getJSONObject(i).getJSONObject("peixe").getLong("id"));
					retirada.setPeixe(peixe);

					if (!posto.equals(Posto.TUNEL.toString())){
						Camara camara = new Camara();
						camara.setId(jListas.getJSONObject(i).getJSONObject("camara").getLong("id"));
						retirada.setCamara(camara);
						
						PosicaoCamara posicaoCamara = new PosicaoCamara();
						posicaoCamara.setId(jListas.getJSONObject(i).getJSONObject("posicaoCamara").getLong("id"));
						retirada.setPosicaoCamara(posicaoCamara);
					}
					
					if (jListas.getJSONObject(i).getJSONObject("embalagem").has("id")) {
						Embalagem embalagem = new Embalagem();
						embalagem.setId(jListas.getJSONObject(i).getJSONObject("embalagem").getLong("id"));
						retirada.setEmbalagem(embalagem);
					}else
						retirada.setEmbalagem(null);

					TipoPeixe tipoPeixe = new TipoPeixe();
					tipoPeixe.setId(jListas.getJSONObject(i).getJSONObject("tipoPeixe").getLong("id"));
					retirada.setTipoPeixe(tipoPeixe);

					if (jListas.getJSONObject(i).getJSONObject("tamanhoPeixe").has("id")) {
						TamanhoPeixe tamanhoPeixe = new TamanhoPeixe();
						tamanhoPeixe.setId(jListas.getJSONObject(i).getJSONObject("tamanhoPeixe").getLong("id"));
						retirada.setTamanhoPeixe(tamanhoPeixe);
					}
					else
						retirada.setTamanhoPeixe(null);

					retiradas.add(retirada);
				}
			}

			ArmazenamentoRepository repository = new ArmazenamentoRepository(manager);
			
			repository.salvarAmazenamentosERetiradas(armazenamentos, retiradas, posto);

			jRetorno.put("valido", true);
		}catch (Exception e){
			e.printStackTrace();
			jRetorno.put("valido", false);
		}finally {
			manager.close();
		}

		return jRetorno.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getNumInconsistencias")
	public String getNumInconsistencias(String dados){
		EntityManager manager = JpaUtil.getEntityManager();
		JSONObject jRetorno = new JSONObject();

		try{
			ArmazenamentoRepository repository = new ArmazenamentoRepository(manager);
			RetiradaRepository retiradaRepository = new RetiradaRepository(manager);
			Filtro filtro = FiltroUtil.getFiltroId(dados);
			Integer numA = repository.getNumInconsistenciasArmazenamentos(filtro);
			Integer numI = retiradaRepository.getNumInconsistenciasRetiradas(filtro);

			jRetorno.put("num", numA + numI);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			manager.close();
		}

		return jRetorno.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getNumComprasInconsistentes")
	public String getNumComprasInconsistentes(String dados){
		EntityManager manager = JpaUtil.getEntityManager();
		JSONObject jRetorno = new JSONObject();

		try{
			CompraRepository repository = new CompraRepository(manager);
			Filtro filtro = FiltroUtil.getFiltroId(dados);
			Integer num = repository.getNumComprasInconsistentes(filtro);

			jRetorno.put("num", num);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			manager.close();
		}

		return jRetorno.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getInconsistenciasArmazenamentosERetiradas")
	public String getInconsistenciasArmazenamentosERetiradas(String dados){

		EntityManager manager = JpaUtil.getEntityManager();
		JSONObject jRetorno = new JSONObject();
		JSONArray jLista = new JSONArray();

		try{
			ArmazenamentoRepository repository = new ArmazenamentoRepository(manager);
			RetiradaRepository retiradaRepository = new RetiradaRepository(manager);
			Filtro filtro = FiltroUtil.getFiltroId(dados);
			List<Armazenamento> armazenamentos = repository.getInconsistenciasArmazenamentos(filtro);
			List<Retirada> retiradas = retiradaRepository.getInconsistenciasRetiradas(filtro);

			for(Armazenamento armazenamento : armazenamentos){
				JSONObject jItemResumo = new JSONObject();
				jItemResumo.put("id", armazenamento.getId());
				jItemResumo.put("tipo", "Armazenar");
				jItemResumo.put("peso", armazenamento.getPeso());
				jItemResumo.put("qtdeEmbalagem", armazenamento.getQtdeEmbalagem());
				jItemResumo.put("pesoEmbalagem", armazenamento.getPesoEmbalagem() != null ? armazenamento.getPesoEmbalagem() : "0");

				JSONObject jPeixe = new JSONObject();
				jPeixe.put("id", armazenamento.getPeixe().getId());
				jPeixe.put("descricao", armazenamento.getPeixe().getDescricao());

				jItemResumo.put("peixe", jPeixe);

				JSONObject jCamara = new JSONObject();
				jCamara.put("id", armazenamento.getCamara().getId());
				jCamara.put("descricao", armazenamento.getCamara().getDescricao());

				jItemResumo.put("camara", jCamara);

				JSONObject jPosicao = new JSONObject();
				jPosicao.put("id", armazenamento.getPosicaoCamara().getId());
				jPosicao.put("descricao", armazenamento.getPosicaoCamara().getDescricao());

				jItemResumo.put("posicaoCamara", jPosicao);

				JSONObject jEmbalagem = new JSONObject();
				if (armazenamento.getEmbalagem() != null) {
					jEmbalagem.put("id", armazenamento.getEmbalagem().getId());
					jEmbalagem.put("descricao", armazenamento.getEmbalagem().getDescricao());

					jItemResumo.put("embalagem", jEmbalagem);
				}

				JSONObject jTipoPeixe = new JSONObject();
				jTipoPeixe.put("id", armazenamento.getTipoPeixe().getId());
				jTipoPeixe.put("descricao", armazenamento.getTipoPeixe().getDescricao());

				jItemResumo.put("tipoPeixe", jTipoPeixe);

				JSONObject jTamanho = new JSONObject();
				if (armazenamento.getTamanhoPeixe() != null) {
					jTamanho.put("id", armazenamento.getTamanhoPeixe().getId());
					jTamanho.put("descricao", armazenamento.getTamanhoPeixe().getDescricao());

					jItemResumo.put("tamanhoPeixe", jTamanho);
				}

				jLista.put(jItemResumo);
		}

			for(Retirada retirada : retiradas){
				JSONObject jItemResumo = new JSONObject();
				jItemResumo.put("id", retirada.getId());
				jItemResumo.put("tipo", "Retirada");
				jItemResumo.put("peso", retirada.getPeso());
				jItemResumo.put("qtdeEmbalagem", retirada.getQtdeEmbalagem());

				JSONObject jPeixe = new JSONObject();
				jPeixe.put("id", retirada.getPeixe().getId());
				jPeixe.put("descricao", retirada.getPeixe().getDescricao());

				jItemResumo.put("peixe", jPeixe);

				JSONObject jCamara = new JSONObject();
				jCamara.put("id", retirada.getCamara().getId());
				jCamara.put("descricao", retirada.getCamara().getDescricao());

				jItemResumo.put("camara", jCamara);

				JSONObject jPosicao = new JSONObject();
				jPosicao.put("id", retirada.getPosicaoCamara().getId());
				jPosicao.put("descricao", retirada.getPosicaoCamara().getDescricao());

				jItemResumo.put("posicaoCamara", jPosicao);

				JSONObject jTipoPeixe = new JSONObject();
				jTipoPeixe.put("id", retirada.getTipoPeixe().getId());
				jTipoPeixe.put("descricao", retirada.getTipoPeixe().getDescricao());

				jItemResumo.put("tipoPeixe", jTipoPeixe);

				jItemResumo.put("destino", retirada.getDestino().equals(TipoDestinoRetirada.PROCESSO) ? "Processo" : "Descarte");

				jLista.put(jItemResumo);
			}

			jRetorno.put("inconsistencias", jLista);

		}catch (Exception e){
			e.printStackTrace();
		}finally {
			manager.close();
		}

		return jRetorno.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getObservacoesArmazenamento")
	public String getObservacoesArmazenamento(String dados){

		EntityManager manager = JpaUtil.getEntityManager();
		JSONObject jRetorno = new JSONObject();
		JSONArray jLista = new JSONArray();

		try{
			ObservacaoArmazenamentoRepository repository = new ObservacaoArmazenamentoRepository(manager);
			Filtro filtro = FiltroUtil.getFiltroId(dados);
			List<ObservacaoArmazenamento> list = repository.getObsArmazenamento(filtro);

			for (ObservacaoArmazenamento obs : list){
				JSONObject object = new JSONObject();
				object.put("data", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(obs.getData()));
				object.put("usuario", obs.getUsuario().getNome());
				object.put("observacao", obs.getObservacao());

				jLista.put(object);
			}

			jRetorno.put("observacoes", jLista);
		}catch (Exception e){

		}finally {
			manager.close();
		}

		return jRetorno.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getObservacoesRetirada")
	public String getObservacoesRetirada(String dados){

		EntityManager manager = JpaUtil.getEntityManager();
		JSONObject jRetorno = new JSONObject();
		JSONArray jLista = new JSONArray();

		try{

			ObservacaoArmazenamentoRepository repository = new ObservacaoArmazenamentoRepository(manager);
			Filtro filtro = FiltroUtil.getFiltroId(dados);
			List<ObservacaoArmazenamento> list = repository.getObsRetirada(filtro);

			for (ObservacaoArmazenamento obs : list){
				JSONObject object = new JSONObject();
				object.put("data", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ").format(obs.getData()));
				object.put("usuario", obs.getUsuario().getNome());
				object.put("observacao", obs.getObservacao());

				jLista.put(object);
			}

			jRetorno.put("observacoes", jLista);

		}catch (Exception e){
			e.printStackTrace();
		}finally {
			manager.close();
		}

		return jRetorno.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getObservacoesCompra")
	public String getObservacoesCompra(String dados){

		EntityManager manager = JpaUtil.getEntityManager();
		JSONObject jRetorno = new JSONObject();
		JSONArray jLista = new JSONArray();

		try{

			CompraRepository repository = new CompraRepository(manager);
			Filtro filtro = FiltroUtil.getFiltroId(dados);
			List<Observacao> list = repository.getObsCompra(filtro);

			for (Observacao obs : list){
				JSONObject object = new JSONObject();
				object.put("data", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(obs.getData()));
				object.put("usuario", obs.getUsuario().getNome());
				object.put("observacao", obs.getObservacao());

				jLista.put(object);
			}

			jRetorno.put("observacoes", jLista);

		}catch (Exception e){
			e.printStackTrace();
		}finally {
			manager.close();
		}

		return jRetorno.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getEstoquePorCamara")
	public String getEstoquePorCamara(String dados){
		EntityManager manager = JpaUtil.getEntityManager();
		JSONObject jRetorno = new JSONObject();
		JSONArray jLista = new JSONArray();
		try {
			ArmazenamentoRepository repository = new ArmazenamentoRepository(manager);
			Filtro filtro = FiltroUtil.getFiltroEstoque(dados);
			List<ItemEstoque> itens = repository.getEstoquePorCamara(filtro);

			for(ItemEstoque itemEstoque : itens){
				JSONObject jArm = new JSONObject();
				jArm.put("peixe", itemEstoque.getPeixe());
				jArm.put("camara", itemEstoque.getCamara());
				jArm.put("tipo", itemEstoque.getTipo());
				jArm.put("peso", itemEstoque.getPeso().toString());
				jArm.put("pesoRetirada", itemEstoque.getPesoRetirada().toString());

				jLista.put(jArm);
			}

			jRetorno.put("estoques", jLista);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			manager.close();
		}

		return jRetorno.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getPedidosPorDia")
	public String getPedidosPorDia(String dados){
		EntityManager manager = JpaUtil.getEntityManager();
		JSONObject jRetorno = new JSONObject();
		JSONArray jLista = new JSONArray();

		try{
			PedidoRepository repository = new PedidoRepository(manager);
			Filtro filtro = FiltroUtil.getFiltroData(dados);
			List<Pedido> pedidos = repository.getPedidosPorDia(filtro);

			for(Pedido pedido : pedidos){
				JSONObject jPedido = new JSONObject();
				jPedido.put("id", pedido.getId());
				jPedido.put("codigo", pedido.getCodigo());
				/*jPedido.put("data", new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(pedido.getData()));
				jPedido.put("valor", pedido.getValor());

				JSONObject jVendedor = new JSONObject();
				jVendedor.put("id", pedido.getVendedor().getId());
				jVendedor.put("nome", pedido.getVendedor().getNome());

				JSONObject jCliente = new JSONObject();
				jCliente.put("id", pedido.getCliente().getId());
				jCliente.put("nome", pedido.getCliente().getNome());

				JSONObject jTransp = new JSONObject();
				jTransp.put("id", pedido.getTransportadora().getId());
				jTransp.put("nome", pedido.getTransportadora().getNome());

				jPedido.put("vendedor", jVendedor);
				jPedido.put("cliente", jCliente);
				jPedido.put("transportadora", jTransp);*/

				jLista.put(jPedido);
			}

			jRetorno.put("pedidos", jLista);

		}catch (Exception e){
			e.printStackTrace();
		}finally {
			manager.close();
		}

		return jRetorno.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getPedidosPorStatus")
	public String getPedidosPorStatus(String dados){
		EntityManager manager = JpaUtil.getEntityManager();
		JSONObject jRetorno = new JSONObject();
		JSONArray jLista = new JSONArray();

		try{
			PedidoRepository repository = new PedidoRepository(manager);
			Filtro filtro = FiltroUtil.getFiltroStatus(dados);
			List<Pedido> pedidos = repository.getPedidosPorStatus(filtro);

			for(Pedido pedido : pedidos){
				JSONObject jPedido = new JSONObject();
				jPedido.put("id", pedido.getId());
				jPedido.put("codigo", pedido.getCodigo());
				jPedido.put("data", new SimpleDateFormat("dd/MM/yyyy").format(pedido.getData()));
				jPedido.put("dataEntrega", new SimpleDateFormat("dd/MM/yyyy").format(pedido.getDataEntrega()));
				/*jPedido.put("valor", pedido.getValor());

				JSONObject jVendedor = new JSONObject();
				jVendedor.put("id", pedido.getVendedor().getId());
				jVendedor.put("nome", pedido.getVendedor().getNome());

				JSONObject jCliente = new JSONObject();
				jCliente.put("id", pedido.getCliente().getId());
				jCliente.put("nome", pedido.getCliente().getNome());

				JSONObject jTransp = new JSONObject();
				jTransp.put("id", pedido.getTransportadora().getId());
				jTransp.put("nome", pedido.getTransportadora().getNome());

				jPedido.put("vendedor", jVendedor);
				jPedido.put("cliente", jCliente);
				jPedido.put("transportadora", jTransp);*/

				jLista.put(jPedido);
			}

			jRetorno.put("pedidos", jLista);

		}catch (Exception e){
			e.printStackTrace();
		}finally {
			manager.close();
		}

		return jRetorno.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getProdutosPorPedido")
	public String getProdutosPorPedido(String dados) {
		EntityManager manager = JpaUtil.getEntityManager();
		JSONObject jRetorno = new JSONObject();
		JSONArray jLista = new JSONArray();

		try{
			ProdutoRepository repository = new ProdutoRepository(manager);
			Filtro filtro = FiltroUtil.getFiltroId(dados);
			List<Produto> produtos = repository.getProdutosPorPedido(filtro);

			for (Produto produto : produtos){
				JSONObject jProduto = new JSONObject();
				jProduto.put("id", produto.getId());
				jProduto.put("peso", produto.getPeso());

				JSONObject jPeixe = new JSONObject();
				jPeixe.put("id", produto.getPeixe().getId());
				jPeixe.put("descricao", produto.getPeixe().getDescricao());
				jPeixe.put("urlFoto", getUrlFoto(manager, produto.getPeixe()));

				JSONObject jTipo = new JSONObject();
				jTipo.put("id", produto.getTipoPeixe().getId());
				jTipo.put("descricao", produto.getTipoPeixe().getDescricao());

				JSONObject jTamanho = new JSONObject();
				jTamanho.put("id", produto.getTamanhoPeixe().getId());
				jTamanho.put("descricao", produto.getTamanhoPeixe().getDescricao());

				/*JSONObject jCamara = new JSONObject();
				jCamara.put("id", produto.getCamara().getId());
				jCamara.put("descricao", produto.getCamara().getDescricao());

				JSONObject jPosicao = new JSONObject();
				jPosicao.put("id", produto.getPosicaoCamara().getId());
				jPosicao.put("descricao", produto.getPosicaoCamara().getDescricao());*/

				jProduto.put("peixe", jPeixe);
				jProduto.put("tipo", jTipo);
				jProduto.put("tamanho", jTamanho);
				//jProduto.put("posicao", jPosicao);

				jLista.put(jProduto);
			}

			jRetorno.put("produtos", jLista);

		}catch (Exception e){
			e.printStackTrace();
		}finally {
			manager.close();
		}

		return jRetorno.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/salvarPedido")
	public String salvarPedido(String dados){
		EntityManager manager = JpaUtil.getEntityManager();
		JSONObject jRetorno = new JSONObject();

		try{
			PedidoRepository repository = new PedidoRepository(manager);
			JSONObject jPedido = new JSONObject(dados);
			Pedido pedido = repository.getPedidoPorId(jPedido.getLong("id"));
			pedido.setIdUsuario(jPedido.getLong("idUsuario"));
			pedido.setStatus(StatusPedido.EMBARCADO);
			pedido.setProdutos(new ArrayList<Produto>());

			//JSONObject jTransp = jPedido.getJSONObject("transportadora");
			//Transportadora transportadora = new Transportadora();
			//transportadora.setId(jTransp.getLong("id"));

			//pedido.setPlacaCarro(jPedido.getString("placaCarro"));
			//pedido.setTransportadora(transportadora);

			JSONArray jProdutos = jPedido.getJSONArray("produtos");

			for (int i = 0; i < jProdutos.length(); i++) {
				Produto produto = new Produto();
				produto.setId(jProdutos.getJSONObject(i).getLong("id"));
				produto.setRomaneios(new ArrayList<Romaneio>());

				JSONArray jRomaneio = jProdutos.getJSONObject(i).getJSONArray("romaneios");

				for (int j = 0; j < jRomaneio.length(); j++) {
					Romaneio romaneio = new Romaneio();
					romaneio.setId(jRomaneio.getJSONObject(j).has("id") ? jRomaneio.getJSONObject(j).getLong("id") : null);
					romaneio.setLote(jRomaneio.getJSONObject(j).getString("lote"));
					romaneio.setQtdeEmbalagens(jRomaneio.getJSONObject(j).getInt("qtdeEmbalagens"));
					romaneio.setPeso(new BigDecimal(jRomaneio.getJSONObject(j).getDouble("peso")));
					romaneio.setProduto(produto);

					Camara camara = new Camara();
					camara.setId(jRomaneio.getJSONObject(j).getJSONObject("camara").getLong("id"));

					PosicaoCamara posicaoCamara = new PosicaoCamara();
					posicaoCamara.setId(jRomaneio.getJSONObject(j).getJSONObject("posicao").getLong("id"));

					TipoPeixe tipo = new TipoPeixe();
					tipo.setId(jRomaneio.getJSONObject(j).getJSONObject("tipo").getLong("id"));

					if (jRomaneio.getJSONObject(i).getJSONObject("tamanhoPeixe").has("id")) {
						TamanhoPeixe tamanhoPeixe = new TamanhoPeixe();
						tamanhoPeixe.setId(jRomaneio.getJSONObject(j).getJSONObject("tamanhoPeixe").getLong("id"));
						romaneio.setTamanhoPeixe(tamanhoPeixe);
					}
					else
						romaneio.setTamanhoPeixe(null);

					romaneio.setCamara(camara);
					romaneio.setPosicaoCamara(posicaoCamara);
					romaneio.setTipoPeixe(tipo);

					produto.getRomaneios().add(romaneio);
				}

				pedido.getProdutos().add(produto);
			}

			String posto = jPedido.getString("posto");
			repository.salvarPedido(pedido, posto);

			jRetorno.put("valido", true);
		}catch (Exception e){
			e.printStackTrace();
			jRetorno.put("valido", false);
		}finally {
			manager.close();
		}

		return jRetorno.toString();
	}

	@POST
	@Path("/uploadArquivo")
	@Consumes("multipart/form-data")
	public String uploadArquivo(@Context HttpServletRequest request) {

		JSONObject jRetorno = new JSONObject();

		EntityManager manager = JpaUtil.getEntityManager();

		try{

			ArquivoRepository repository = new ArquivoRepository(manager);

			salvar(request);

			jRetorno.put("valido", true);
		}catch (Exception e){
			e.printStackTrace();
			jRetorno.put("valido", false);
		}

		return "ok";
	}

	public void salvar(HttpServletRequest request){
		if (ServletFileUpload.isMultipartContent(request)){
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> itens = null;

			try{
				itens = upload.parseRequest(request);
			}catch (FileUploadException e){
				e.printStackTrace();
			}

			if (itens != null){
				for (FileItem item : itens){
					if (!item.isFormField() && item.getSize() > 0){
						String fileName = "image_" + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()) + "." + item.getName();
						File tempFile = new File(System.getProperty("java.io.tmpdir"), fileName);
						try{
							item.write(tempFile);
						}catch (Exception e){
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

}
