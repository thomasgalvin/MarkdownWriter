import com.galvin.markdown.model.ContributorRole;
import com.galvin.markdown.model.Contributor;
import com.galvin.markdown.model.Node;

Node node = controller.getNodeForCurrentDocument();
if( node != null )
{
    String name = "Thomas Galvin";
    String sortByName = "Galvin, Thomas";
    ContributorRole role = ContributorRole.AUTHOR;
    Contributor contributor = new Contributor( name, sortByName, role );
    
    node.getContributors().clear();
    node.getContributors().add( contributor );
}

