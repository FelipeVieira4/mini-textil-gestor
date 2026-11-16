package com.minitextil.gestor.fornecedor.view;

import com.minitextil.gestor.components.core.program.ProgramErp;
import com.minitextil.gestor.components.grid.GridComponent;
import com.minitextil.gestor.components.grid.TipoColunas;
import com.minitextil.gestor.fornecedor.model.Fornecedor;
import com.minitextil.gestor.fornecedor.repository.FornecedorRepository;
import com.minitextil.gestor.genericos.utils.CnpjUtils;
import com.minitextil.gestor.genericos.utils.TelefoneUtils;
import com.vaadin.flow.component.Component;

import java.util.List;

public class ConsultaFornecedor extends ProgramErp {
    private final GridComponent<Fornecedor> grid = new GridComponent<>(Fornecedor.class);
    private final FornecedorRepository repository;

    public ConsultaFornecedor(FornecedorRepository repository) {
        this.repository = repository;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        setCabecalho("Consulta de Fornecedor");

        configurarGrid();
        add(grid);

        carregarClientes();
    }

    private void configurarGrid() {
        grid.addColunaComFiltro(Fornecedor::getId, "ID", TipoColunas.Numerico);
        grid.addColunaComFiltro(Fornecedor::getDescricao, "Razão Social / Nome", TipoColunas.Alfanumerico);
        grid.addColunaComFiltro(Fornecedor::getSituacao, "Situação", TipoColunas.Boolean);
        grid.addColunaComFiltro(c -> CnpjUtils.formatar(c.getCnpj()), "CNPJ", TipoColunas.Alfanumerico);
        grid.addColunaComFiltro(c -> TelefoneUtils.formatar(c.getTelefone()), "Telefone", TipoColunas.Alfanumerico);
        grid.addColunaComFiltro(Fornecedor::getEmail, "Email", TipoColunas.Alfanumerico);
        grid.addColunaComFiltro(cliente -> cliente.getEndereco().getUf(), "UF", TipoColunas.Alfanumerico);
    }

    private void carregarClientes() {
        List<Fornecedor> listaClientes = repository.findAll();
        grid.setItensComFiltro(listaClientes);
    }

    @Override
    public Component getView() {
        return this;
    }
}