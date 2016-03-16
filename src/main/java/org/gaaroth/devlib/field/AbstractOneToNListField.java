package org.gaaroth.devlib.field;

import java.lang.reflect.Field;
import java.util.Collection;

import org.gaaroth.devlib.utils.ReflectionUtils;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings({"serial", "unchecked", "rawtypes"})
public abstract class AbstractOneToNListField<M> extends CustomField<Collection<M>> {

	public VerticalLayout mainLayout;
	public GeneratedPropertyContainer gpc;
	public Grid grid;
	public BeanItemContainer<M> listContainer;
	public Class<M> clazz;
	public String parentFieldName;	
	
	public AbstractOneToNListField(Class<M> clazz, String parentFieldName){
        this.clazz = clazz;
        this.parentFieldName = parentFieldName;
		
		mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("250px");
		
		listContainer = new BeanItemContainer<M>(clazz);
		
		grid = new Grid();
		gpc = new GeneratedPropertyContainer(listContainer);
		configureGrid();
		grid.setContainerDataSource(gpc);
		grid.setSizeFull();
		
		mainLayout.addComponent(grid);
		mainLayout.setExpandRatio(grid, 1);
		
	}
	
	@Override
	public Object getConvertedValue() {
        // no need to convert
        return getValue();
    }
	
	@Override
	public void setInternalValue(Collection<M> collection) throws com.vaadin.data.Property.ReadOnlyException, ConversionException {
		super.setInternalValue(collection);
		listContainer.removeAllItems();
		if (collection != null) {
			listContainer.addAll(collection);
		}
	}
	
	@Override
    public Collection<M> getValue() {
		// the super implementation invokes getInternalValue() (via getFieldValue())
        // but only if (dataSource == null || isBuffered() || isModified())
        // otherwise it calls convertFromModel(getDataSourceValue())
        // which is a *private* pass-through for propertyDataSource.getValue()

        if (getPropertyDataSource() == null || isBuffered() || isModified()) {
            // return the buffered value
            return getInternalValue();
        }

        // not buffered, nor modified, just return the underlying value
        // no conversion is needed
        return (Collection<M>) getPropertyDataSource().getValue();
    }
	
	@Override
    protected Collection<M> getInternalValue() {
		BeanItemContainer<M> container = getListContainer();
        
        // the value must be pulled from the internal collection
        // we know this is a ListContainer, so we just get all the Ids == all the values
        Collection<M> allItems = container.getItemIds(); // ListContainer returns the list of all the values
        Collection<M> internalValue = super.getInternalValue();
        
        if (internalValue == allItems) {
            return internalValue;
        } else if (internalValue == null && (allItems == null || allItems.size() == 0)) {
            return null;
        } else if (internalValue == null && allItems != null && allItems.size()>0) {
        	internalValue = allItems;
        	return internalValue;
        } else {
        	internalValue.clear();
            internalValue.addAll(allItems);
            return internalValue;
        }
        
        /*
        // this is only needed during initialization/binding
        // there is a moment during which the internal state IS NOT consistent
        // (caused by setPropertyDataSource())
        // the internalValue is null, but the listContainer contains an empty collection
        // we'll just ignore the inconsistency, and return null
        if (internalValue == null) {
            return null;
        }
        
        // if they are the same instance, no need to copy the values over
        // i.e.: if the collection returned by container.getItemIds()
        //       is the same as the internal value, then there is no
        //       need to copy over the values; we can just return the collection itself.
        if (internalValue == allItems) {
            return internalValue;
        }

        internalValue.clear();
        internalValue.addAll(allItems);

        return internalValue;
        */
    }

	@Override
	protected Component initContent() {		
		return mainLayout;
	}

	@Override
	public Class getType() {
		return Collection.class;
		//return PersistentBag.class;		//se hibernate
		//return AbstractList.class;	//se eclipseLink
	}
	
	protected BeanItemContainer<M> getListContainer() {
        return listContainer;
    }
	
	public void setParentInModel(M model) {
		Field fieldParent = ReflectionUtils.getField(clazz, parentFieldName);
		try {
			fieldParent.set(model, getParentModel());
		} catch (Exception e) {
			throw new RuntimeException("Impossibile settare parent nel model");
		}
	}

	public abstract void configureGrid();
	public abstract Object getParentModel();
	
}
