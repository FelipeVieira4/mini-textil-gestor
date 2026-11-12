package com.minitextil.gestor.item.material.view;

import org.springframework.data.domain.Sort;

import com.minitextil.gestor.components.core.program.ProgramContext;
import com.minitextil.gestor.components.core.program.ProgramErp;
import com.minitextil.gestor.components.core.program.ProgramParams;
import com.minitextil.gestor.components.grid.GridComponent;
import com.minitextil.gestor.components.grid.TipoColunas;
import com.minitextil.gestor.item.material.model.Material;
import com.minitextil.gestor.item.material.repository.MaterialRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;

public class ConsultaMaterial extends ProgramErp {

    private static final long serialVersionUID = 1L;

    private final GridComponent<Material> grid = new GridComponent<>(Material.class);
    private final MaterialRepository repository;

    private ProgramContext context;

    public ConsultaMaterial(MaterialRepository repository) {
        this.repository = repository;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        setCabecalho("Consulta de Materiais", "Duplo clique em uma linha para abrir o cadastro");

        Button btAtualizar = new Button("Atualizar", VaadinIcon.REFRESH.create(), _ -> carregarMateriais());
        btAtualizar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        configurarGrid();
        add(btAtualizar, grid);
        setFlexGrow(1, grid);

        carregarMateriais();
    }

    private void configurarGrid() {
        grid.setColumnReorderingAllowed(true);

        grid.addColunaComFiltro(Material::getId, "ID", TipoColunas.Numerico);
        grid.addColunaComFiltro(Material::getDescricao, "Descrição", TipoColunas.Alfanumerico);
        grid.addColunaComFiltro(Material::getUnidade, "Uni", TipoColunas.Alfanumerico);
        grid.addColunaComFiltro(Material::isAtivo, "Ativo", TipoColunas.Boolean);

        grid.addItemDoubleClickListener(event -> abrirCadastro(event.getItem()));
    }

    private void carregarMateriais() {
        grid.setItensComFiltro(repository.findAll(Sort.by("id")));
    }

    private void abrirCadastro(Material material) {
        if (context == null) {
            return;
        }
        // ao fechar o cadastro, recarrega a lista (o material pode ter sido alterado)
        context.openProgram("CADASTRO_MATERIAL",
                new ProgramParams().set("idItem", material.getId()),
                _ -> carregarMateriais());
    }

    @Override
    public Component getView() {
        return this;
    }

    @Override
    public void onOpen(ProgramParams params, ProgramContext context) {
        this.context = context;
    }
}
