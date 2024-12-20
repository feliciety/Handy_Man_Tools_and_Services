package project.demo.controllers.Cart;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import project.demo.controllers.Cart.CartPageController;
import project.demo.controllers.Main.MainStructureController;
import project.demo.models.CartItem;
import project.demo.models.CartManager;
import project.demo.models.Product;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class CartTableController {

    @FXML
    private TableView<CartItem> cartTable;

    @FXML
    public AnchorPane contentPane;

    @FXML
    private TableColumn<CartItem, ImageView> productImageCol;

    @FXML
    private TableColumn<CartItem, String> productNameCol;

    @FXML
    private TableColumn<CartItem, HBox> productQuantityCol;

    @FXML
    private TableColumn<CartItem, String> productPriceCol;

    @FXML
    private TableColumn<CartItem, Void> deleteButtonCol;

    private final ObservableList<CartItem> cartItems = CartManager.getInstance().getCartItems();

    private CartPageController mainController;
    private MainStructureController mainStructureController; // Reference for main structure navigation

    public void setMainStructureController(MainStructureController mainStructureController) {
        this.mainStructureController = mainStructureController;
        if (this.mainStructureController != null) {
            System.out.println("[DEBUG] MainStructureController has been properly set in CartTableController.");
        } else {
            System.out.println("[ERROR] Failed to set MainStructureController in CartTableController.");
        }
    }
    @FXML
    public void initialize() {

        cartTable.setRowFactory(tv -> {
            TableRow<CartItem> row = new TableRow<>() {
                @Override
                protected void updateItem(CartItem item, boolean empty) {
                    super.updateItem(item, empty);
                    setPrefHeight(75); // Set desired row height here
                }
            };
            return row;
        });
        // Set up table columns with centered alignment
        productImageCol.setCellValueFactory(new PropertyValueFactory<>("productImage"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantityControl"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        // Center all columns, including the quantity column
        centerColumnContent(productImageCol);
        centerColumnContent(productNameCol);
        centerQuantityColumn(productQuantityCol); // Special method to center HBox
        centerColumnContent(productPriceCol);

        // Add Delete Button Column with centered alignment
        addDeleteButtonToTable();

        // Bind the cartItems from CartManager to the table
        cartTable.setItems(cartItems);
    }

    private void addDeleteButtonToTable() {
        deleteButtonCol.setCellFactory(tc -> new TableCell<>() {
            private final Button deleteButton = new Button("X");

            {
                deleteButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5;");
                deleteButton.setOnAction(event -> {
                    CartItem cartItem = getTableView().getItems().get(getIndex());
                    removeCartItem(cartItem);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
                setAlignment(Pos.CENTER);
            }
        });
    }

    /**
     * Centers the content of a TableColumn.
     */
    private <T> void centerColumnContent(TableColumn<CartItem, T> column) {
        column.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else if (item instanceof Node) {
                    setGraphic((Node) item);
                    setText(null);
                } else {
                    setText(item.toString());
                    setGraphic(null);
                }
                setAlignment(Pos.CENTER); // Center align content
            }
        });
    }

    /**
     * Special method to center an HBox in a TableColumn.
     */
    private void centerQuantityColumn(TableColumn<CartItem, HBox> column) {
        column.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(HBox hbox, boolean empty) {
                super.updateItem(hbox, empty);
                if (empty || hbox == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Align the HBox itself
                    hbox.setAlignment(Pos.CENTER);
                    setGraphic(hbox);
                }
                setAlignment(Pos.CENTER); // Center align the cell
            }
        });
    }

    /**
     * Removes a cart item from the table.
     */
    public void removeCartItem(CartItem cartItem) {
        System.out.println("Removing: " + cartItem.getProductName());
        cartItems.remove(cartItem);
        updateTable();
    }

    /**
     * Refreshes the table to reflect updates.
     */
    public void updateTable() {
        cartTable.refresh();
    }


    public void setMainController(CartPageController mainController) {
        this.mainController = mainController;
        System.out.println("[DEBUG] Main controller set in CartTableController.");
    }

    /**
     * Navigates to the Details page.
     */
    @FXML
    public void goToDetails(ActionEvent actionEvent) {
        mainController.goToDetails();
    }

    @FXML
    private void goToShop() {
        if (mainStructureController != null) {
            mainStructureController.navigateTo("/project/demo/FXMLShopPage/ShopPage.fxml");
        } else {
            System.err.println("[ERROR] MainStructureController is not set in CartTableController.");
        }
    }
}