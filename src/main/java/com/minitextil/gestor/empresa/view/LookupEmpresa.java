package com.minitextil.gestor.empresa.view;

import java.util.List;
import java.util.function.Consumer;

import com.minitextil.gestor.components.grid.GridComponent;
import com.minitextil.gestor.components.grid.TipoColunas;
import com.minitextil.gestor.empresa.model.Empresa;
import com.minitextil.gestor.empresa.repository.EmpresaRepository;
import com.vaadin.flow.component.dialog.Dialog;

public class LookupEmpresa {
		
	    public void VerTodasEmpresas(EmpresaRepository empresaRepository, Consumer<Empresa> selecionarListener) {
	        GridComponent<Empresa> grid = new GridComponent<>(Empresa.class);
			
	        Dialog dialog = new Dialog();
	        dialog.setHeaderTitle("Operadores");
	        dialog.setWidth("280px");
			
	        List<Empresa> listaEmpresas= empresaRepository.findAll();
			
	        grid.setColumnReorderingAllowed(true);
	        grid.addColunaComFiltro(Empresa::getId, "ID", TipoColunas.Numerico);
	        grid.addColunaComFiltro(Empresa::getDescricao, "Nome", TipoColunas.Alfanumerico);
			
	        
	        grid.addItemDoubleClickListener(event -> {
	            Empresa selecionado = event.getItem();
	            if (selecionarListener != null) {
	            	selecionarListener.accept(selecionado);
	            }
	            dialog.close();
	        });
			
	        dialog.setWidth("60vw");
	        dialog.setHeight("40vh");
	        
	        grid.setItensComFiltro(listaEmpresas);
	        dialog.add(grid);
	        
	        dialog.open();
	    }
	}