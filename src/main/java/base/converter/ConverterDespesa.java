package base.converter;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;
import dao.GenericDAO;
import base.modelo.Despesa; 
import util.Mensagem;

@Named("converterDespesa")
public class ConverterDespesa implements Converter{

	@Inject
	private GenericDAO<Despesa> dao;

	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		if (value != null && value.trim().length() > 0) {
			try {
				
			 return dao.buscarPorId(Despesa.class, Long.parseLong(value));
			 
			} catch (Exception e) {
				e.printStackTrace();
				throw new ConverterException(
						new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO_CONVERTER, ""));
			}
		} else {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (!(value instanceof Despesa)) return null;
		    return  String.valueOf(((Despesa) value).getId());
		
		
	}
}
