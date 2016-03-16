package org.gaaroth.devlib.field;

import org.gaaroth.devlib.custom.CustomFieldFactory;
import org.gaaroth.devlib.notifications.CustomDialogs;
import org.gaaroth.devlib.notifications.CustomNotifications;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings({"serial", "unchecked"})
public abstract class FormInlineOneToNField<M> extends AbstractOneToNListField<M> {

	public HorizontalLayout menuBar;
	public Button nuovoModel, modificaModel, eliminaModel;
	public ClickListener nuovoClickListener, modificaClickListener, eliminaClickListener;
	
	public FormInlineOneToNField(final Class<M> clazz, String parentFieldName){
		super(clazz, parentFieldName);
		
		grid.setEditorEnabled(true);
		grid.setEditorFieldFactory(new CustomFieldFactory());
//		grid.addEditorListener(new EditorListener() {
//			@Override
//			public void editorOpened(EditorOpenEvent e) {
//			}
//			
//			@Override
//			public void editorMoved(EditorMoveEvent e) { }
//			
//			@Override
//			public void editorClosed(EditorCloseEvent e) {
//				nuovoModel.setEnabled(true);
//				getState().modified=true;
//			}
//		});
		
		grid.setEditorSaveCaption("Salva");
		grid.setEditorCancelCaption("Annulla");
		
        menuBar = new HorizontalLayout();
        menuBar.setWidth("100%");
		
		nuovoModel = new Button("Nuovo", FontAwesome.PLUS);
		nuovoModel.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		
		nuovoClickListener = new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (grid.isEditorActive()) {
					grid.cancelEditor();
				}
				M model = getNewModel();
				setParentInModel(model);
				listContainer.addBean(model); //BeanItem<M> newItemID = 
				grid.editItem(listContainer.getBeanIdResolver().getIdForBean(model));
			}
		};
		nuovoModel.addClickListener(nuovoClickListener);
		
		modificaModel = new Button("Modifica", FontAwesome.PENCIL_SQUARE_O);
		modificaModel.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		modificaClickListener = new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (grid.isEditorActive()) {
					grid.cancelEditor();
				}
				if (grid.getSelectedRow() != null) {
					grid.editItem(grid.getSelectedRow());
				} else {
					CustomNotifications.showWarning("E' necessario selezionare una riga");
				}
			}
		};
		modificaModel.addClickListener(modificaClickListener);
		
		eliminaModel = new Button("Elimina", FontAwesome.TRASH_O);
		eliminaModel.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		eliminaClickListener = new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				CustomDialogs.showConfirm("Confermate l'eliminazione?", new ConfirmDialog.Listener() {
					@Override
					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							listContainer.removeItem(grid.getSelectedRow());
						}
					}
				});
			}
		};
		eliminaModel.addClickListener(eliminaClickListener);
		menuBar.addComponents(nuovoModel, modificaModel, eliminaModel);
		menuBar.setExpandRatio(eliminaModel, 1);
		
		grid.addSelectionListener(new SelectionListener() {
			@Override
			public void select(SelectionEvent event) {
				setButtonsEnabled(event.getSelected().size() > 0);
			}
		});
		setButtonsEnabled(false);
		
		mainLayout.addComponent(menuBar,0);
	}

	public void setButtonsEnabled(boolean flag) {
		modificaModel.setEnabled(flag);
		eliminaModel.setEnabled(flag);
	}

	public abstract M getNewModel();
	
}
