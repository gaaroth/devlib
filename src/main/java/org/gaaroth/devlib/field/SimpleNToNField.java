package org.gaaroth.devlib.field;

import javax.persistence.EntityManager;

import org.gaaroth.devlib.notifications.CustomDialogs;
import org.gaaroth.devlib.notifications.CustomNotifications;
import org.gaaroth.devlib.utils.ReflectionUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;

import it.zero11.vaadin.asyncfiltercombobox.AsyncFilterComboBox;
import it.zero11.vaadin.asyncfiltercombobox.AsyncFilterComboBox.FilterChangeEventMode;

@SuppressWarnings({"serial", "unchecked"})
public abstract class SimpleNToNField<M, F> extends AbstractNToNListField<M, F> {

	public HorizontalLayout menuBar;
	public AsyncFilterComboBox selectModel;
	public Button aggiungiModel, eliminaModel;
	public boolean checkDuplicati;
	
	public SimpleNToNField(Class<M> clazz, String parentFieldName, Class<F> clazzChild, String childFieldName, String descrizionePropertyId){
		super(clazz, parentFieldName, clazzChild, childFieldName);
		
        menuBar = new HorizontalLayout();
        menuBar.setWidth("100%");
        
        selectModel = new AsyncFilterComboBox();
        selectModel.setWidth("100%");
        selectModel.setFilterChangeTimeout(1000);
        selectModel.setFilterChangeEventMode(FilterChangeEventMode.LAZY);
        selectModel.setFilteringMode(FilteringMode.CONTAINS);
		final JPAContainer<F> container = JPAContainerFactory.makeReadOnly(clazzChild, getEntityManager());
		selectModel.setConverter(new SingleSelectConverter<F>(selectModel));
		selectModel.setContainerDataSource(container);
		selectModel.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		
		if (descrizionePropertyId == null) {
			selectModel.setItemCaptionPropertyId(ReflectionUtils.getDescrizionePropertyIdFromModel(clazzChild));
		} else {
			selectModel.setItemCaptionPropertyId(descrizionePropertyId);
		}
		
		aggiungiModel = new Button("Aggiungi", FontAwesome.PLUS);
		aggiungiModel.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		aggiungiModel.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		aggiungiModel.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				//TODO checks
				if (selectModel.getValue() != null) {
					if (true) {//TODO !listContainer.containsId(selectModel.getValue())  || !checkDuplicati
						M model = getNewModel();
						F child = (F) container.getItem(selectModel.getValue()).getEntity();
						setParentInModel(model);
						setChildInModel(model, child);
						listContainer.addBean(model);//Object newItemID = 
					}
//					else {
//						CustomNotifications.showWarning(getLocalizedStrings().valoreGiaPresente);
//					}
				} else {
					CustomNotifications.showWarning("E' necessario selezionare un valore");
				}
			}
		});
		
		eliminaModel = new Button("Elimina", FontAwesome.TRASH_O);
		eliminaModel.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		eliminaModel.addStyleName(ValoTheme.BUTTON_DANGER);
		eliminaModel.addClickListener(new ClickListener() {
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
		menuBar.addComponents(selectModel, aggiungiModel, eliminaModel);
		menuBar.setComponentAlignment(eliminaModel, Alignment.MIDDLE_RIGHT);
		
		mainLayout.addComponent(menuBar,0);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		menuBar.setVisible(enabled);
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		menuBar.setVisible(!readOnly);
	}
	
	public boolean isCheckDuplicati() {
		return checkDuplicati;
	}

	public void setCheckDuplicati(boolean checkDuplicati) {
		this.checkDuplicati = checkDuplicati;
	}

	public abstract EntityManager getEntityManager();
	public abstract M getNewModel();

}
