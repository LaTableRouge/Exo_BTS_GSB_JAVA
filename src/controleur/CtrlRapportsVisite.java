package controleur;


import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import modele.dao.*;
import modele.metier.*;
import vue.VueRapportsVisite;

//Chaque controleur rapporte les données du DAO, se l'approprie et l'affiche dans la vue

//Contrôleur de la fenêtre CtrlRapportsVisite
public class CtrlRapportsVisite extends CtrlAbstrait 
{
    private List<Medicament> lesMedicaments = new ArrayList<Medicament>();
   //instancie l'objet daoRapportsVisite
   private DaoRapportsVisite daoRapportsVisite = new DaoRapportsVisite();

    /**
     *
     * @param ctrlPrincipal
     */
   
   //Constructeur de CtrlRapportsVisite
    public CtrlRapportsVisite(CtrlPrincipal ctrlPrincipal)
    {
        //Super car appelle une instance du contrôle principal
        super(ctrlPrincipal);
        vue = new VueRapportsVisite(this);
        actualiser();
    }

    public final void actualiser() 
    {
        try 
        {
            chargerListeRapportsVisite();
        } 
        catch (DaoException ex)
        {
            //message d'erreur en cas de fail d'actualisation des données
            JOptionPane.showMessageDialog(getVue(), "CtrlRapportsVisite - actualiser - " + ex.getMessage(), "", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * RapportsVisiteFermer réaction au clic sur le bouton Fermer de la vueRapportsVisite
     * Le contrôle est rendu au contrôleur frontal
     */
    public void RapportsVisiteFermer()
    {
        this.getCtrlPrincipal().action(EnumAction.RAPPORTSVISITE_FERMER);
    }
   
    //Bouton suivant. rajoute + 1 a chaque Rapports Visite
    //en se basant sur la liste déroulante
    public void RapportsVisiteSuivant()
    {
        int index = getVue().getjComboBoxRapportsVisite().getSelectedIndex()+1;
        if(index== getVue().getjComboBoxRapportsVisite().getItemCount())index=0;
        getVue().getjComboBoxRapportsVisite().setSelectedIndex(index);
    }
    
    
    //Bouton précédent enleve -1 a chaque Rapports Visite
    //en se basant sur la liste déroulante
    public void RapportsVisitePrecedent()
    {
        int index = getVue().getjComboBoxRapportsVisite().getSelectedIndex()-1;
        if(index== -1) index=getVue() .getjComboBoxRapportsVisite() .getItemCount() -1;
        getVue().getjComboBoxRapportsVisite().setSelectedIndex(index);
    }
    /**
     * chargerListeRapportsVisite renseigner le modèle du composant jComboBoxNumero
     * à partir de la base de données (DaoRapportsVisite)
     *
     * @throws DaoException
     */
    //si ca ne marche pas, sort un message d'erreur (throws)
    private void chargerListeRapportsVisite() throws DaoException 
    {
        //le RapportsVisite utilisé correspond à la classe métier RapportsVisite
        // est nommé "desRapportsVisite"
        List<RapportsVisite> desRapportsVisite = daoRapportsVisite.getAll();
        //Remplace les données par suppresion et insertion de données
        //renseigne le champs déroulant de la vue
        getVue().getModeleJComboBoxRapportsVisite().removeAllElements();
        for (RapportsVisite unRapportsVisite : desRapportsVisite) 
        {
            getVue().getModeleJComboBoxRapportsVisite().addElement(unRapportsVisite);
        }
    }
    /**
     * RapportsVisiteSelectionner renseigner le modèle des composants
     * JTextField (TxtNom(Renvoie le nom + prénom dans 1 seul champs), TxtDaterapport, TxtMotifvisite, TxtBilan),
     *
     */
    public void RapportsVisiteSelectionner()
    { 
        RapportsVisite rapportsVisiteSelect = (RapportsVisite) getVue().getjComboBoxRapportsVisite().getSelectedItem();
        
        if (rapportsVisiteSelect != null)
        { 
            //getVue().getTxtNom().setText(rapportsVisiteSelect.toString());
            getVue().getTxtNumero().setText(Integer.toString((int)rapportsVisiteSelect.getNumrapport()));
            getVue().getTxtDaterapport().setText(rapportsVisiteSelect.getDaterapport());    
            getVue().getTxtMotifvisite().setText(rapportsVisiteSelect.getMotifvisite());
            getVue().getTxtBilan().setText(rapportsVisiteSelect.getBilan());
            getVue().getTxtOffre().setText(rapportsVisiteSelect.getOffre());
        }       
    }
    
    public void menuPraticien()
    {
        this.getCtrlPrincipal().action(EnumAction.MENU_PRATICIENS);
    }
    
    //effacer tous les champs
     private void effacerElements(Container container) 
     {
        for (Component component : container.getComponents())
        {
            if (component instanceof JFormattedTextField)
            {
                JFormattedTextField formattedText = (JFormattedTextField) component;
                if (formattedText.getName() != null && formattedText.getName().equals("numRapport")) 
                {
                    try 
                    {
                        formattedText.setValue(daoRapportsVisite.recupererDernierID() + 1);
                    } 
                    catch (DaoException ex) 
                    {
                        Logger.getLogger(CtrlRapportsVisite.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (formattedText.getName() != null && formattedText.getName().equals("dateRapport")) 
                {
                    Date maintenant = new Date();
                    formattedText.setValue(maintenant);
                }

            } 
            else if (component instanceof JTextField) 
            {
                ((JTextField) component).setText("");
            } 
            else if (component instanceof JTextArea) 
            {
                ((JTextArea) component).setText("");
            }
            if (component instanceof Container) 
            {
                effacerElements((Container) component);
            }
        }
        getVue().getTxtNom().setSelectedItem(null);
    }
    
     //efface tous les champs
      public void nouveauRapport() 
      {
        effacerElements((Container) getVue());
    }
     
 
   
    @Override
    public VueRapportsVisite getVue() 
    {
        return (VueRapportsVisite) vue;
    }
}