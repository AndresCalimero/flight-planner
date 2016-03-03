package dad.planeador.vuelos.services;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aed.componentes.db.ConnectionConfig;
import aed.componentes.db.DatabaseConnection;
import aed.componentes.db.DatabaseConnectionException;
import dad.planeador.vuelos.models.Aeropuerto;
import dad.planeador.vuelos.models.Aerovia;
import dad.planeador.vuelos.models.Avion;
import dad.planeador.vuelos.models.Coordenadas;
import dad.planeador.vuelos.models.Interseccion;
import dad.planeador.vuelos.models.Navaid;
import dad.planeador.vuelos.models.Pista;
import dad.planeador.vuelos.models.PlanDeVuelo;
import dad.planeador.vuelos.models.Punto;
import dad.planeador.vuelos.models.TipoAvion;
import dad.planeador.vuelos.models.TipoNavaid;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PlaneadorVuelosService {

	private static DatabaseConnection db;

	// Key: ICAO
	private static final Map<String, Aeropuerto> AEROPUERTOS = new HashMap<>();
	private static final ObservableList<Aeropuerto> LISTA_AEROPUERTOS = FXCollections.observableArrayList();

	// Key: Hash code
	private static final Map<Integer, Interseccion> INTERSECCIONES = new HashMap<>();
	private static final ObservableList<Interseccion> LISTA_INTERSECCIONES = FXCollections.observableArrayList();

	// Key: Codigo
	private static final Map<Long, TipoNavaid> TIPOS_NAVAIDS = new HashMap<>();
	private static final ObservableList<TipoNavaid> LISTA_TIPOS_NAVAIDS = FXCollections.observableArrayList();

	// Key: Hash code
	private static final Map<Integer, Navaid> NAVAIDS = new HashMap<>();
	private static final ObservableList<Navaid> LISTA_NAVAIDS = FXCollections.observableArrayList();

	// Key: Codigo
	private static final Map<Long, TipoAvion> TIPOS_AVION = new HashMap<>();
	private static final ObservableList<TipoAvion> LISTA_TIPOS_AVION = FXCollections.observableArrayList();

	// Key: Numero registro
	private static final Map<String, Avion> AVIONES = new HashMap<>();
	private static final ObservableList<Avion> LISTA_AVIONES = FXCollections.observableArrayList();

	// Key: Identificador
	private static final Map<String, Aerovia> AEROVIAS = new HashMap<>();
	private static final ObservableList<Aerovia> LISTA_AEROVIAS = FXCollections.observableArrayList();

	// Key: Codigo
	private static final Map<Long, PlanDeVuelo> PLANES_DE_VUELO = new HashMap<>();
	private static final ObservableList<PlanDeVuelo> LISTA_PLANES_DE_VUELO = FXCollections.observableArrayList();

	private PlaneadorVuelosService() {
	}

	public static void cargarDatos() {
		ConnectionConfig cc = new ConnectionConfig(DatabaseConnection.DB_SQLITE);
		cc.setFile(new File("resources/planeadorvuelosdb.sqlite3"));
		try {
			db = DatabaseConnection.getDatabaseConnection(cc);
			cargarAeropuertos();
			cargarIntersecciones();
			cargarTiposNavaids();
			cargarNavaids();
			cargarTiposAvion();
			cargarAviones();
			cargarAerovias();
			cargarPlanesDeVuelo();
			generarListasObservables();
		} catch (DatabaseConnectionException | SQLException e) {
			e.printStackTrace();
		}
	}

	private static void cargarAeropuertos() throws SQLException {
		ResultSet rs = db.query("SELECT icao, nombre, grados_norte, grados_este FROM aeropuertos");
		while (rs.next()) {
			Aeropuerto aeropuerto = new Aeropuerto();
			aeropuerto.setIcao(rs.getString("icao"));
			aeropuerto.setNombre(rs.getString("nombre"));
			aeropuerto.getCoordenadas().setGradosNorte(rs.getDouble("grados_norte"));
			aeropuerto.getCoordenadas().setGradosEste(rs.getDouble("grados_este"));
			cargarPistas(aeropuerto);
			AEROPUERTOS.put(aeropuerto.getIcao(), aeropuerto);
		}
	}

	private static void cargarPistas(Aeropuerto aeropuerto) throws SQLException {
		ResultSet rs = db.query("SELECT pista, grados_norte, grados_este FROM pistas WHERE icao = ?",
				aeropuerto.getIcao());
		while (rs.next()) {
			Pista pista = new Pista();
			pista.setIcao(aeropuerto.getIcao());
			pista.setPista(rs.getString("pista"));
			pista.getCoordenadas().setGradosNorte(rs.getDouble("grados_norte"));
			pista.getCoordenadas().setGradosEste(rs.getDouble("grados_este"));
			aeropuerto.getPistas().add(pista);
		}
	}

	private static void cargarIntersecciones() throws SQLException {
		ResultSet rs = db.query("SELECT codigo, identificador, grados_norte, grados_este FROM intersecciones");
		while (rs.next()) {
			Interseccion interseccion = new Interseccion();
			interseccion.setCodigo(rs.getLong("codigo"));
			interseccion.setTipo(Punto.TIPO_INTERSECCION);
			interseccion.setIdentificador(rs.getString("identificador"));
			interseccion.getCoordenadas().setGradosNorte(rs.getDouble("grados_norte"));
			interseccion.getCoordenadas().setGradosEste(rs.getDouble("grados_este"));
			INTERSECCIONES.put(interseccion.hashCode(), interseccion);
		}
	}

	private static void cargarTiposNavaids() throws SQLException {
		ResultSet rs = db.query("SELECT codigo, tipo FROM tipos_navaid");
		while (rs.next()) {
			TipoNavaid tipoNavaid = new TipoNavaid();
			tipoNavaid.setCodigo(rs.getLong("codigo"));
			tipoNavaid.setTipo(rs.getString("tipo"));
			TIPOS_NAVAIDS.put(tipoNavaid.getCodigo(), tipoNavaid);
		}
	}

	private static void cargarNavaids() throws SQLException {
		ResultSet rs = db.query("SELECT * FROM navaids");
		while (rs.next()) {
			Navaid navaid = new Navaid();
			navaid.setCodigo(rs.getLong("codigo"));
			navaid.setNombre(rs.getString("nombre"));
			navaid.setTipo(Punto.TIPO_NAVAID);
			navaid.setTipoNavaid(TIPOS_NAVAIDS.get(rs.getLong("tipo")));
			navaid.setIdentificador(rs.getString("identificador"));
			int frecuencia = rs.getInt("frecuencia");
			if (frecuencia != 0) {
				String frecuenciaS = frecuencia + "";
				frecuenciaS = frecuenciaS.substring(0, 3) + "." + frecuenciaS.substring(3);
				navaid.setFrecuencia(frecuenciaS);
			}
			navaid.getCoordenadas().setGradosNorte(rs.getDouble("grados_norte"));
			navaid.getCoordenadas().setGradosEste(rs.getDouble("grados_este"));
			NAVAIDS.put(navaid.hashCode(), navaid);
		}
	}

	private static void cargarTiposAvion() throws SQLException {
		ResultSet rs = db.query("SELECT codigo, tipo FROM tipos_avion");
		while (rs.next()) {
			TipoAvion tipoAvion = new TipoAvion();
			tipoAvion.setCodigo(rs.getLong("codigo"));
			tipoAvion.setTipo(rs.getString("tipo"));
			TIPOS_AVION.put(tipoAvion.getCodigo(), tipoAvion);
		}
	}

	private static void cargarAviones() throws SQLException {
		ResultSet rs = db.query("SELECT * FROM aviones");
		while (rs.next()) {
			Avion avion = new Avion();
			avion.setNumeroRegistro(rs.getString("num_registro"));
			avion.setTipoAvion(TIPOS_AVION.get(rs.getLong("tipo")));
			avion.setDow(rs.getLong("dow"));
			avion.setMzfw(rs.getLong("mzfw"));
			avion.setMtow(rs.getLong("mtow"));
			avion.setMlw(rs.getLong("mlw"));
			avion.setPasajeros(rs.getInt("pasajeros"));
			avion.setCarga(rs.getLong("carga"));
			avion.setCombustible(rs.getLong("combustible"));
			avion.setConsumoAPUHora(rs.getInt("consumo_apu_hora"));
			AVIONES.put(avion.getNumeroRegistro(), avion);
		}
	}

	private static void cargarAerovias() throws SQLException {
		ResultSet rs = db.query("SELECT * FROM aerovias ORDER BY identificador, orden ASC");
		Aerovia ultima = null;
		while (rs.next()) {
			if (ultima == null || !ultima.getIdentificador().equals(rs.getString("identificador"))) {
				Aerovia aerovia = new Aerovia();
				aerovia.setIdentificador(rs.getString("identificador"));
				if (rs.getLong("codigo_interseccion") != 0) {
					agregarInterseccionAerovia(rs.getLong("codigo_interseccion"), aerovia);
				} else {
					agregarNavaidAerovia(rs.getLong("codigo_navaid"), aerovia);
				}
				ultima = aerovia;
				AEROVIAS.put(aerovia.getIdentificador(), aerovia);
			} else {
				if (rs.getLong("codigo_interseccion") != 0) {
					agregarInterseccionAerovia(rs.getLong("codigo_interseccion"), ultima);
				} else {
					agregarNavaidAerovia(rs.getLong("codigo_navaid"), ultima);
				}
			}
		}
	}

	private static void agregarInterseccionAerovia(long codigoInterseccion, Aerovia aerovia) throws SQLException {
		Coordenadas coordenadas = new Coordenadas();
		ResultSet rs = db.query("SELECT identificador, grados_norte, grados_este FROM intersecciones WHERE codigo = ?",
				codigoInterseccion);
		coordenadas.setGradosNorte(rs.getDouble("grados_norte"));
		coordenadas.setGradosEste(rs.getDouble("grados_este"));
		aerovia.getPuntos()
				.add(INTERSECCIONES.get(Punto.generateHashCode(codigoInterseccion, Punto.TIPO_INTERSECCION)));
	}

	private static void agregarNavaidAerovia(long codigoNavaid, Aerovia aerovia) throws SQLException {
		Coordenadas coordenadas = new Coordenadas();
		ResultSet rs = db.query("SELECT identificador, grados_norte, grados_este FROM navaids WHERE codigo = ?",
				codigoNavaid);
		coordenadas.setGradosNorte(rs.getDouble("grados_norte"));
		coordenadas.setGradosEste(rs.getDouble("grados_este"));
		aerovia.getPuntos().add(NAVAIDS.get(Punto.generateHashCode(codigoNavaid, Punto.TIPO_NAVAID)));
	}

	private static void cargarPlanesDeVuelo() throws SQLException {
		ResultSet rs = db.query("SELECT * FROM planes_vuelo");
		while (rs.next()) {
			PlanDeVuelo planDeVuelo = new PlanDeVuelo();
			planDeVuelo.setAeropuertoDestino(AEROPUERTOS.get(rs.getString("aeropuerto_destino")));
			planDeVuelo.setAeropuertoOrigen(AEROPUERTOS.get(rs.getString("aeropuerto_origen")));
			planDeVuelo.setAltitudCrucero(rs.getInt("altitud_crucero"));
			planDeVuelo.setAvion(AVIONES.get(rs.getString("avion")));
			planDeVuelo.setCarga(rs.getInt("carga"));
			planDeVuelo.setCodigo(rs.getLong("codigo"));
			planDeVuelo.setEquipaje(rs.getInt("equipaje"));
			planDeVuelo.setNumeroAdultos(rs.getInt("numero_adultos"));
			planDeVuelo.setNumeroBebes(rs.getInt("numero_bebes"));
			planDeVuelo.setNumeroChildren(rs.getInt("numero_ninos"));
			planDeVuelo.setNumeroVuelo(rs.getString("numero_vuelo"));
			planDeVuelo.setObservacionesAvion(rs.getString("observaciones_avion"));
			Pista pistaAterrizaje = null;
			for (Pista p : planDeVuelo.getAeropuertoDestino().getPistas()) {
				if (p.getPista().equals(rs.getString("pista_aterrizaje"))) {
					pistaAterrizaje = p;
					break;
				}
			}
			planDeVuelo.setPistaAterrizaje(pistaAterrizaje);
			Pista pistaDespegue = null;
			for (Pista p : planDeVuelo.getAeropuertoOrigen().getPistas()) {
				if (p.getPista().equals(rs.getString("pista_despegue"))) {
					pistaDespegue = p;
					break;
				}
			}
			planDeVuelo.setPistaDespegue(pistaDespegue);
			planDeVuelo.setRutaATC(rs.getString("ruta_atc"));
			PLANES_DE_VUELO.put(planDeVuelo.getCodigo(), planDeVuelo);
		}
	}

	private static void generarListasObservables() {
		LISTA_AEROPUERTOS.addAll(AEROPUERTOS.values());
		LISTA_AEROVIAS.addAll(AEROVIAS.values());
		LISTA_AVIONES.addAll(AVIONES.values());
		LISTA_INTERSECCIONES.addAll(INTERSECCIONES.values());
		LISTA_NAVAIDS.addAll(NAVAIDS.values());
		LISTA_PLANES_DE_VUELO.addAll(PLANES_DE_VUELO.values());
		LISTA_TIPOS_AVION.addAll(TIPOS_AVION.values());
		LISTA_TIPOS_NAVAIDS.addAll(TIPOS_NAVAIDS.values());
	}

	public static void guardarAvion(Avion avion) throws PlaneadorVuelosServiceException {
		if (LISTA_AVIONES.contains(avion)) {
			try {
				db.update(
						"UPDATE aviones SET tipo = ?, dow = ?, mzfw = ?, mtow = ?, mlw = ?, pasajeros = ?, carga = ?, combustible = ?, consumo_apu_hora = ? WHERE num_registro = ?",
						avion.getTipoAvion().getCodigo(), avion.getDow(), avion.getMzfw(), avion.getMtow(),
						avion.getMlw(), avion.getPasajeros(), avion.getCarga(), avion.getCombustible(),
						avion.getConsumoAPUHora(), avion.getNumeroRegistro());
			} catch (SQLException e) {
				throw new PlaneadorVuelosServiceException(e.getMessage(), e);
			}
		} else {
			LISTA_AVIONES.add(avion);
			try {
				db.insert(
						"INSERT INTO aviones(tipo, dow, mzfw, mtow, mlw, pasajeros, carga, combustible, consumo_apu_hora, num_registro) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
						avion.getTipoAvion().getCodigo(), avion.getDow(), avion.getMzfw(), avion.getMtow(),
						avion.getMlw(), avion.getPasajeros(), avion.getCarga(), avion.getCombustible(),
						avion.getConsumoAPUHora(), avion.getNumeroRegistro());
			} catch (SQLException e) {
				throw new PlaneadorVuelosServiceException(e.getMessage(), e);
			}
		}
	}

	public static void eliminarAvion(Avion avion) throws PlaneadorVuelosServiceException {
		if (LISTA_AVIONES.contains(avion)) {
			for (PlanDeVuelo plan : LISTA_PLANES_DE_VUELO) {
				if (avion.equals(plan.getAvion())) {
					plan.setAvion(null);
					guardarPlanDeVuelo(plan);
				}
			}
			LISTA_AVIONES.remove(avion);
			try {
				db.update("DELETE FROM aviones WHERE num_registro = ?", avion.getNumeroRegistro());
			} catch (SQLException e) {
				throw new PlaneadorVuelosServiceException(e.getMessage(), e);
			}
		}
	}

	public static void guardarTipoAvion(TipoAvion tipo) throws PlaneadorVuelosServiceException {
		if (LISTA_TIPOS_AVION.contains(tipo)) {
			try {
				db.update("UPDATE tipos_avion SET tipo = ? WHERE codigo = ?", tipo.getTipo(), tipo.getCodigo());
			} catch (SQLException e) {
				throw new PlaneadorVuelosServiceException(e.getMessage(), e);
			}
		} else {
			LISTA_TIPOS_AVION.add(tipo);
			try {
				ResultSet rs = db.insert("INSERT INTO tipos_avion(tipo) VALUES(?)", tipo.getTipo());
				rs.next();
				tipo.setCodigo(rs.getLong(1));
			} catch (SQLException e) {
				throw new PlaneadorVuelosServiceException(e.getMessage(), e);
			}
		}
	}

	public static void eliminarTipoAvion(TipoAvion tipo) throws PlaneadorVuelosServiceException {
		if (LISTA_TIPOS_AVION.contains(tipo)) {
			List<Avion> aviones = new ArrayList<>(LISTA_AVIONES);
			for (Avion avion : aviones) {
				if (avion.getTipoAvion().equals(tipo)) {
					eliminarAvion(avion);
				}
			}

			LISTA_TIPOS_AVION.remove(tipo);
			try {
				db.update("DELETE FROM tipos_avion WHERE codigo = ?", tipo.getCodigo());
			} catch (SQLException e) {
				throw new PlaneadorVuelosServiceException(e.getMessage(), e);
			}
		}
	}

	public static void guardarPlanDeVuelo(PlanDeVuelo plan) throws PlaneadorVuelosServiceException {
		if (LISTA_PLANES_DE_VUELO.contains(plan)) {
			try {
				db.update(
						"UPDATE planes_vuelo SET avion = ?, aeropuerto_origen = ?, aeropuerto_destino = ?, altitud_crucero = ?, ruta_atc = ?, numero_vuelo = ?, pista_despegue = ?, pista_aterrizaje = ?, observaciones_avion = ?, numero_adultos = ?, numero_ninos = ?, numero_bebes = ?, equipaje = ?, carga = ? WHERE codigo = ?",
						(plan.getAvion() != null ? plan.getAvion().getNumeroRegistro() : null),
						plan.getAeropuertoOrigen().getIcao(), plan.getAeropuertoDestino().getIcao(),
						plan.getAltitudCrucero(), plan.getRutaATC(), plan.getNumeroVuelo(),
						(plan.getPistaDespegue() != null ? plan.getPistaDespegue().getPista() : null),
						(plan.getPistaAterrizaje() != null ? plan.getPistaAterrizaje().getPista() : null),
						plan.getObservacionesAvion(), (plan.getNumeroAdultos() == 0 ? null : plan.getNumeroAdultos()),
						(plan.getNumeroChildren() == 0 ? null : plan.getNumeroChildren()),
						(plan.getNumeroBebes() == 0 ? null : plan.getNumeroBebes()),
						(plan.getEquipaje() == 0 ? null : plan.getEquipaje()),
						(plan.getCarga() == 0 ? null : plan.getCarga()), plan.getCodigo());
			} catch (SQLException e) {
				throw new PlaneadorVuelosServiceException(e.getMessage(), e);
			}
		} else {
			LISTA_PLANES_DE_VUELO.add(plan);
			try {
				ResultSet rs = db.insert(
						"INSERT INTO planes_vuelo(avion, aeropuerto_origen, aeropuerto_destino, altitud_crucero, ruta_atc, numero_vuelo, pista_despegue, pista_aterrizaje, observaciones_avion, numero_adultos, numero_ninos, numero_bebes, equipaje, carga) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
						(plan.getAvion() != null ? plan.getAvion().getNumeroRegistro() : null),
						plan.getAeropuertoOrigen().getIcao(), plan.getAeropuertoDestino().getIcao(),
						plan.getAltitudCrucero(), plan.getRutaATC(), plan.getNumeroVuelo(),
						(plan.getPistaDespegue() != null ? plan.getPistaDespegue().getPista() : null),
						(plan.getPistaAterrizaje() != null ? plan.getPistaAterrizaje().getPista() : null),
						plan.getObservacionesAvion(), (plan.getNumeroAdultos() == 0 ? null : plan.getNumeroAdultos()),
						(plan.getNumeroChildren() == 0 ? null : plan.getNumeroChildren()),
						(plan.getNumeroBebes() == 0 ? null : plan.getNumeroBebes()),
						(plan.getEquipaje() == 0 ? null : plan.getEquipaje()),
						(plan.getCarga() == 0 ? null : plan.getCarga()));
				rs.next();
				plan.setCodigo(rs.getLong(1));
			} catch (SQLException e) {
				throw new PlaneadorVuelosServiceException(e.getMessage(), e);
			}
		}
	}

	public static void eliminarPlanDeVuelo(PlanDeVuelo plan) throws PlaneadorVuelosServiceException {
		if (LISTA_PLANES_DE_VUELO.contains(plan)) {
			LISTA_PLANES_DE_VUELO.remove(plan);
			try {
				db.update("DELETE FROM planes_vuelo WHERE codigo = ?", plan.getCodigo());
			} catch (SQLException e) {
				throw new PlaneadorVuelosServiceException(e.getMessage(), e);
			}
		}
	}

	public static ObservableList<Aeropuerto> getListaAeropuertos() {
		return LISTA_AEROPUERTOS;
	}

	public static ObservableList<Interseccion> getListaIntersecciones() {
		return LISTA_INTERSECCIONES;
	}

	public static ObservableList<TipoNavaid> getListaTiposNavaids() {
		return LISTA_TIPOS_NAVAIDS;
	}

	public static ObservableList<Navaid> getListaNavaids() {
		return LISTA_NAVAIDS;
	}

	public static ObservableList<TipoAvion> getListaTiposAvion() {
		return LISTA_TIPOS_AVION;
	}

	public static ObservableList<Avion> getListaAviones() {
		return LISTA_AVIONES;
	}

	public static ObservableList<Aerovia> getListaAerovias() {
		return LISTA_AEROVIAS;
	}

	public static ObservableList<PlanDeVuelo> getListaPlanesDeVuelo() {
		return LISTA_PLANES_DE_VUELO;
	}

}
