/**
 * 
 */
package us.muit.fs.a4i.model.entities;

import java.io.IOException;
import java.time.LocalDateTime;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.Collection;

import us.muit.fs.a4i.config.Context;
import us.muit.fs.a4i.exceptions.MetricException;
import us.muit.fs.a4i.model.entities.Indicator.IndicatorBuilder;
import us.muit.fs.a4i.model.entities.ReportItemI;
/**
 * @author Isabel Rom醤
 * Entidad para guardar la informaci髇 de un indicador o una m閠rica, elementos de un informe
 *
 */
public class ReportItem<T> implements ReportItemI<T>{
	private Indicator<T> indicator = null;
	private static Logger log=Logger.getLogger(ReportItem.class.getName());
	/**
	 * Nombre del indicador/m閠rica
	 */
	private String name;
	/**
	 * Valor del indicador/m閠rica
	 */
	private T value;
	/**
	 * Obligatorio
	 * Fecha en la que se construye el objeto o se toma la medida
	 */
	private Date date;
	/**
	 * Descripci髇 del elemento del informe
	 */
	private String description;
	/**
	 * Origen del elemento
	 */
	private String source;
	/**
	 * Unidades de medida
	 */
	private String unit;

	/**
	 * Construye un objeto ReportItem a partir de un constructor, previamente configurado
	 * S贸lo lo utiliza el propio constructor, es privado, nadie, que no sea el constructor, puede crear una ReportItem
	 * @param builder Constructor del ReportItem
	 */
	private ReportItem(ReportItemBuilder<T> builder){
		
		this.description=builder.description;
		this.name=builder.name;
		this.value=builder.value;
		this.source=builder.source;
		this.unit=builder.unit;
		this.date=builder.date;
	}
	
	/**
	 * Obtiene la descripci贸n del ReportItem
	 * @return Descripci贸n del significado del ReportItem
	 */
	public String getDescription() {
		return description;
	}
	
	
	/**
	 * Consulta el nombre del ReportItem
	 * @return Nombre del ReportItem
	 */
	public String getName() {
		return name;
	}
	/**
	 * Consulta el valor del ReportItem
	 * @return ReportItem
	 */
	public T getValue() {
		return value;
	}
	/**
	 * Consulta la fuente de informaci贸n
	 * @return Origen del ReportItem
	 */
	public String getSource() {
		return source;
	}
	/***
	 * Establece la fuente de la informaci贸n para el ReportItem
	 * @param source fuente de informaci贸n origen
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * Consulta las unidades del ReportItem
	 * @return la unidad usada en el ReportItem
	 */
	public String getUnit() {
		return unit;
	}


	/**
	 * Consulta el indicador de ReportItem
	 * @return indicador de ReportItem
	 */
	public Indicator<T> getIndicator() {
		return indicator;
	}

	
	/**
	 * Consulta cuando se obtuvo el ReportItem
	 * @return Fecha de consulta del ReportItem
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * <p>Clase para construir ReportItem. Verifica los ReportItem antes de crearlos</p>
	 *
	 */
	public static class ReportItemBuilder<T>{
		private String description;
		private String name;
		private Date date;
		private T value;
		private String source;
		private String unit;
		private Collection<ReportItem> metrics;
		private ReportItem metric;
		private IndicatorBuilder state;
		public ReportItemBuilder(String name, T value) throws MetricException {
			HashMap<String,String> reportItemDefinition=null;
			/**
			 * Verifico si el elemento est� definido y el tipo es correcto
			 */
			//el nombre incluye java.lang, si puede eliminar si se manipula la cadena
			//hay que quedarse s贸lo con lo que va detr谩s del 煤ltimo punto o meter en el fichero el nombre completo
			//Pero 驴y si se usan tipos definidos en otras librer铆as? usar el nombre completo "desambigua" mejor
			log.info("Verifico el ReportItem de nombre "+name+" con valor de tipo "+value.getClass().getName());
			try {
			reportItemDefinition=Context.getContext().getChecker().definedReportItem(name,value.getClass().getName());
					
			if(reportItemDefinition!=null) {				
				this.name=name;
				this.value=value;			
				this.date=Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
				this.description=reportItemDefinition.get("description");
				this.unit=reportItemDefinition.get("unit");
			}else {
				throw new MetricException("M閠rica "+name+" no definida o tipo "+value.getClass().getName()+" incorrecto");
			}
			}catch(IOException e) {
				throw new MetricException("El fichero de configuraci髇 de ReportItem no se puede abrir");
			}
			
			
		}
		/**
		 * <p>Establece la descripci贸n del ReportItem</p>
		 * @param description Breve descripci贸n del significado del ReportItem
		 * @return El propio constructor
		 */
		public ReportItemBuilder<T> description(String description){
			this.description=description;
			return this;
		}


		/**
		 * <p>Establece la fecha del ReportItem</p>
		 * @param date Fecha del ReportItem
		 * @return El propio constructor
		 */
		public ReportItemBuilder<T> date(Date date){
			this.date=date;
			return this;
		}


		/**
		 * <p>Establece el estado del ReportItem</p>
		 * @param state Estado del ReportItem
		 * @return El propio constructor
		 */
		public ReportItemBuilder<T> state(IndicatorBuilder<T> state){
			this.state=state;
			return this;
		}


		/**
		 * <p>Establece la m茅trica del ReportItem</p>
		 * @param metric M茅trica del ReportItem
		 * @return El propio constructor
		 */
		public ReportItemBuilder<T> metric(ReportItem<T> metric){
			this.metric=metric;
			return this;
		}


		/**
		 * <p>Establece la fuente de informaci贸n</p>
		 * @param source Fuente de la que se extrajeron los datos
		 * @return El propio constructor
		 */
		public ReportItemBuilder<T> source(String source){
			this.source=source;
			return this;
		}
		/**
		 * <p>Establece las unidades de medida</p>
		 * @param unit Unidades de medida del ReportItem
		 * @return El propio constructor
		 */
		public ReportItemBuilder<T> unit(String unit){
			this.unit=unit;
			return this;
		}
		public ReportItem<T> build(){
			return new ReportItem<T>(this);			
		}
	}
	
	@Override
	public String toString() {
		String info;
		info="ReportItem para "+description+", con valor=" + value + ", source=" + source
				+ ", unit=" + unit +" fecha de la medida=  "+ date;
		return info;
	}
	
}
