package com.minitextil.gestor.operador.view;

import com.minitextil.gestor.components.core.program.ProgramErp;
import com.minitextil.gestor.components.grid.GridComponent;
import com.minitextil.gestor.components.grid.TipoColunas;
import com.minitextil.gestor.operador.model.Operador;
import com.minitextil.gestor.operador.repository.OperadorRepository;
import com.vaadin.flow.component.Component;

import java.util.List;

@Deprecated
public class ConsultaOperador extends ProgramErp {

    private static final long serialVersionUID = 1L;

    private final GridComponent<Operador> grid = new GridComponent<>(Operador.class);
    private final OperadorRepository repository;

    public ConsultaOperador(OperadorRepository operadorRepository) {
        this.repository = operadorRepository;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        setCabecalho("Consulta de Operadores", "Consulta de operadores");

        configurarGrid();
        add(grid);

        carregarOperadores();
    }

    private void configurarGrid() {
        grid.setColumnReorderingAllowed(true);

        grid.addColunaComFiltro(Operador::getId, "ID", TipoColunas.Numerico);
        grid.addColunaComFiltro(Operador::getNome, "Nome", TipoColunas.Alfanumerico);
        grid.addColunaComFiltro(Operador::getEmail, "Email", TipoColunas.Alfanumerico);
    }

    private void carregarOperadores() {
        List<Operador> listaOperadores = repository.findAll();
        grid.setItensComFiltro(listaOperadores);
    }

    @Override
    public Component getView() {
        return this;
    }
}