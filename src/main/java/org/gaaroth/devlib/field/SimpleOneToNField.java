package org.gaaroth.devlib.field;

import org.gaaroth.devlib.custom.CustomFieldFactory;
import org.gaaroth.devlib.notifications.CustomDialogs;
import org.gaaroth.devlib.notifications.CustomNotifications;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings({ "serial", "unchecked" })
public abstract class SimpleOneToNField<C> extends AbstractOneToNListField<C> {
	
	public FieldGroup binderField;
	public Button salvaButton, modificaButton, eliminaButton;
	public BeanItem<C> currentBean;
	public MHorizontalLayout overGridLayout;

	public SimpleOneToNField(Class<C> clazz, String parentFieldName) {
		super(clazz, parentFieldName);
		
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.setEditorEnabled(false);
		
		binderField = new FieldGroup();
		binderField.setFieldFactory(new CustomFieldFactory());
		
		refreshForm();
		
		salvaButton = new Button("Salva", FontAwesome.SAVE);
		salvaButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		salvaButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					binderField.commit();
					if (listContainer.getItem(currentBean) == null) {
						setParentInModel(currentBean.getBean());
						listContainer.addBean(currentBean.getBean());
					}
					refreshForm();
				} catch (CommitException e) {
					CustomNotifications.showError("Dati incompleti. Ricontrollare.");
				} catch (Exception e) {
					CustomNotifications.showError("Errore durante il salvataggio. Contattare l'assitenza.");
				}
			}
		});
		
		modificaButton = new Button("Modifica", FontAwesome.EDIT);
		modificaButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		modificaButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (grid.getSelectedRow() == null) return;
				currentBean = listContainer.getItem(grid.getSelectedRow());
				binderField.setItemDataSource(currentBean);
			}
		});
		
		eliminaButton = new Button("Elimina", FontAwesome.TRASH_O);
		eliminaButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		eliminaButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (grid.getSelectedRow() == null) return;
				CustomDialogs.showConfirm("Confermate l'eliminazione?", new ConfirmDialog.Listener() {
					@Override
					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							listContainer.removeItem(grid.getSelectedRow());
						}
					}
				});
			}
		});
		
		Component overGridForm = prepareOvergridFrom();
		
		overGridLayout = new MHorizontalLayout(overGridForm, salvaButton, modificaButton, eliminaButton);
		overGridLayout.setExpandRatio(overGridForm, 1);
		overGridLayout.setComponentAlignment(salvaButton, Alignment.BOTTOM_RIGHT);
		overGridLayout.setComponentAlignment(modificaButton, Alignment.BOTTOM_RIGHT);
		overGridLayout.setComponentAlignment(eliminaButton, Alignment.BOTTOM_RIGHT);
		mainLayout.addComponent(overGridLayout, 0);		
		
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		overGridLayout.setVisible(enabled);
		overGridLayout.setEnabled(enabled);
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		overGridLayout.setVisible(!readOnly);
		overGridLayout.setEnabled(!readOnly);
	}
	
	public void refreshForm() {
		currentBean = new BeanItem<C>(getNewModel());
		binderField.setItemDataSource(currentBean);
	}

	public abstract Component prepareOvergridFrom();
	
	public abstract C getNewModel();

}
