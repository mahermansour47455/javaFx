package com.airplane.demo;

import com.airplane.demo.entities.Vol;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Date;
import java.util.ResourceBundle;

public class EscaleController implements Initializable {
    @FXML
    private Button AddEscale;

    @FXML
    private Button RetournerAuVol;

    @FXML
    private TextField Aeroport;

    @FXML
    private TableColumn<Vol,String> ColAA;

    @FXML
    private TableColumn<Vol,String> ColAd;

    @FXML
    private TableColumn<Vol, String> ColDA;

    @FXML
    private TableColumn<Vol, String> ColDD;

    @FXML
    private TableColumn<Vol,String> ColHA;

    @FXML
    private TableColumn<Vol,String> ColHD;



    @FXML
    private Button SelectBtn;

    @FXML
    private TableView<Vol> TablVol;

    @FXML
    private TableColumn<Vol,Integer> colId;

    @FXML
    private TableColumn<Vol,String> colEtat;

    @FXML
    private TextField heureArriver;

    @FXML
    private TextField heureDepart;
    @FXML
    private TextField aeroportArriver;

    @FXML
    private TextField aeroportDepart;

    @FXML
    private DatePicker dateArriver;

    @FXML
    private DatePicker datedepart;

    @FXML
    private TextField vharriver;

    @FXML
    private TextField vhdepart;

    @FXML
    private Button btnaddvol;

    @FXML
    void addVol(ActionEvent event) {
        PreparedStatement st=null;
        ResultSet rs=null;
        Connection con = Connexion.getConnexionn();
        String dateArriver = this.dateArriver.getValue().toString();
        String datedepart = this.datedepart.getValue().toString();
        String varriver = this.aeroportArriver.getText();
        String vdepart = this.aeroportDepart.getText();
        String vharriver = this.vharriver.getText();
        String vhdepart = this.vhdepart.getText();
        try{

            String sql="INSERT INTO vol(aerropDepart,aeroportArrivee,heureDepart,heureArrivee,dateDepart,dateArrivee) VALUES (?,?,?,?,?,?)";
            st=con.prepareStatement(sql);
            st.setString(1, vdepart);
            st.setString(2, varriver);
            st.setString(3, vhdepart);
            st.setString(4, vharriver);
            st.setString(5, datedepart);
            st.setString(6, dateArriver);
            int e=st.executeUpdate();
            if(e == 1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Vol added successfully");
                alert.showAndWait();
                TablVol.setItems(getVols());
                aeroportArriver.setText("");
                aeroportDepart.setText("");
               this. vharriver.setText("");
              this.  vhdepart.setText("");



            }
            else{
                System.out.println("error");
            }

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(cellData -> cellData.getValue().idVolProperty().asObject());
        ColAd.setCellValueFactory(cellData -> cellData.getValue().aeroportArriveeProperty());
        ColAA.setCellValueFactory(cellData -> cellData.getValue().aerropDepartProperty());
        ColHA.setCellValueFactory(cellData -> cellData.getValue().dateArriveeProperty());
        ColHD.setCellValueFactory(cellData -> cellData.getValue().dateDepartProperty());
        ColDA.setCellValueFactory(cellData -> cellData.getValue().heureArriveeProperty());
        ColDD.setCellValueFactory(cellData -> cellData.getValue().heureDepartProperty());
        colEtat.setCellValueFactory(cellData -> cellData.getValue().etatProperty());
        TablVol.setItems(getVols());
    }

    public void AffecterEscaleAuVol() {
        int idVol = TablVol.getSelectionModel().getSelectedItem().getIdVol();
        String aeroport = Aeroport.getText();
        String heureA = heureArriver.getText();
        String heureD = heureDepart.getText();

        Connection con = Connexion.getConnexionn();
        PreparedStatement preparedStatement = null;

        try {
            String query = "INSERT INTO escale (nomAeroport, heureDepart, heureArrivee, idVol) VALUES (?, ?, ?, ?)";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, aeroport);
            preparedStatement.setString(2, heureA);
            preparedStatement.setString(3, heureD);
            preparedStatement.setInt(4, idVol);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Escale ajoutée avec succès");
                alert.showAndWait();
                 Aeroport.setText("");
                heureArriver.setText("");
                 heureDepart.setText("");


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatement != null) {
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private ObservableList<Vol> getVols() {
        ObservableList<Vol> vols = FXCollections.observableArrayList();

            Connection con = Connexion.getConnexionn();
            Statement statement = null;
            ResultSet resultSet = null;
            try {
                statement = con.createStatement();
                resultSet = statement.executeQuery("SELECT * FROM `vol`");
                while (resultSet.next()) {
                    int id = resultSet.getInt("idVol");
                    String aeroportArriver = resultSet.getString("aeroportArrivee");
                    String aeroportDepart = resultSet.getString("aerropDepart");
                    String dateArriver = resultSet.getString("dateArrivee");
                    String dateDepart = resultSet.getString("dateDepart");
                    String heureArriver = resultSet.getString("heureArrivee");
                    String heureDepart = resultSet.getString("heureDepart");
                    String etat = resultSet.getString("etat");


                    Vol vol = new Vol(id, aeroportArriver, aeroportDepart, dateArriver, dateDepart, heureArriver, heureDepart, etat);
                    vols.add(vol);

                }


            } catch (Exception e) {
                e.printStackTrace();

                }

        return vols;
    }

    public void RetournerAuVol(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Vous allez être redirigé vers la page des vols");
        alert.showAndWait();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("compagnie.fxml"));
        Parent home = fxmlLoader.load();
        Scene homeScene = new Scene(home, 1000, 800);

        Stage currentStage = (Stage) RetournerAuVol.getScene().getWindow();
        currentStage.setTitle("Home");
        currentStage.setScene(homeScene);
        currentStage.show();

    }

    public void FermerVol(ActionEvent actionEvent) {
        if (TablVol.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un vol");
            alert.showAndWait();
            return;
        } else {
            if (TablVol.getSelectionModel().getSelectedItem().getEtat().equals("Fermé")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Ce vol est déjà fermé");
                alert.showAndWait();
                return;
            } else {
                // get the selected item (idVol
                int idVol = TablVol.getSelectionModel().getSelectedItem().getIdVol();

                Connection con = Connexion.getConnexionn();
                PreparedStatement preparedStatement = null;

                try {
                    String query = "UPDATE vol SET etat = ? WHERE idVol = ?";
                    preparedStatement = con.prepareStatement(query);
                    preparedStatement.setString(1, "Fermé");
                    preparedStatement.setInt(2, idVol);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Vol fermé avec succès");
                        alert.showAndWait();
                        TablVol.setItems(getVols());

                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (preparedStatement != null) {
                        }
                        if (con != null) {
                            con.close();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }
    @FXML
    public void OuvrirVolBtn(ActionEvent event)
    {
        if (TablVol.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un vol");
            alert.showAndWait();
            return;
        } else {
            if (TablVol.getSelectionModel().getSelectedItem().getEtat().equals("Ouvert")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Ce vol est déjà ouvert");
                alert.showAndWait();
                return;
            } else {
                int idVol = TablVol.getSelectionModel().getSelectedItem().getIdVol();

                Connection con = Connexion.getConnexionn();
                PreparedStatement preparedStatement = null;

                try {
                    String query = "UPDATE vol SET etat = ? WHERE idVol = ?";
                    preparedStatement = con.prepareStatement(query);
                    preparedStatement.setString(1, "Ouvert");
                    preparedStatement.setInt(2, idVol);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Vol ouvert avec succès");
                        alert.showAndWait();
                        TablVol.setItems(getVols());

                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (preparedStatement != null) {
                        }
                        if (con != null) {
                            con.close();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }

    @FXML
    public void ModifierVol(ActionEvent event)
    {
        if (TablVol.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un vol");
            alert.showAndWait();
            return;
        } else {
            if (TablVol.getSelectionModel().getSelectedItem().getEtat().equals("Fermé")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Ce vol est fermé");
                alert.showAndWait();
                return;
            } else {
                int idVol = TablVol.getSelectionModel().getSelectedItem().getIdVol();

                Connection con = Connexion.getConnexionn();
                PreparedStatement preparedStatement = null;


                try {
                    String query = "UPDATE vol SET aeroportArrivee = ?, aerropDepart = ?, heureArrivee = ?, heureDepart = ? WHERE idVol = ?";
                    preparedStatement = con.prepareStatement(query);
                    preparedStatement.setString(1, aeroportArriver.getText());
                    preparedStatement.setString(2, aeroportDepart.getText());
                    preparedStatement.setString(3, heureArriver.getText());
                    preparedStatement.setString(4, heureDepart.getText());
                    preparedStatement.setInt(5, idVol);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Vol modifié avec succès");
                        alert.showAndWait();
                        TablVol.setItems(getVols());

                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (preparedStatement != null) {
                        }
                        if (con != null) {
                            con.close();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }



    }
    @FXML
    public void SupprmerVolBtn(ActionEvent event)
    {
        if (TablVol.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un vol");
            alert.showAndWait();
            return;
        } else {
            if (TablVol.getSelectionModel().getSelectedItem().getEtat().equals("Fermé")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Ce vol est fermé");
                alert.showAndWait();
                return;
            } else {
                int idVol = TablVol.getSelectionModel().getSelectedItem().getIdVol();

                Connection con = Connexion.getConnexionn();
                PreparedStatement preparedStatement = null;

                try {
                    String query = "DELETE FROM vol WHERE idVol = ?";
                    preparedStatement = con.prepareStatement(query);
                    preparedStatement.setInt(1, idVol);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Vol supprimé avec succès");
                        alert.showAndWait();
                        TablVol.setItems(getVols());

                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (preparedStatement != null) {
                        }
                        if (con != null) {
                            con.close();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }
}

