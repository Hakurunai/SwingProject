package fr.cda.view;

import fr.cda.controller.GUIController;
import fr.cda.model.OperationResult;
import fr.cda.model.Product;
import fr.cda.view.swing.SwingViewConfig;

import javax.swing.*;
import java.util.function.Consumer;

public class UpdateProductDialog extends ProductDialog
{
    /**
     * @param config     The initial configuration of the {@link JDialog}
     * @param controller Injection of the {@link GUIController}
     * @param frameOwner The {@link JFrame} owning the internal {@link JDialog}
     * @param consumer   A {@link Consumer} who can be called by any child class if necessary
     */
    public UpdateProductDialog(SwingViewConfig config, GUIController controller, JFrame frameOwner, Consumer<OperationResult<Product>> consumer)
    {
        super(config, controller, frameOwner, consumer);
    }

    @Override
    protected void SetValidateButtonListener()
    {

    }
}
