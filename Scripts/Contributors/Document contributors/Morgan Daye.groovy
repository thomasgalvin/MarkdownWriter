import com.galvin.markdown.model.ContributorRole;
import com.galvin.markdown.model.Contributor;
import com.galvin.markdown.model.Node;

Node node = controller.getNodeForCurrentDocument();
if( node != null )
{
    String name = "Morgan Daye";
    String sortByName = "Daye, Morgan";
    ContributorRole role = ContributorRole.AUTHOR;
    Contributor contributor = new Contributor( name, sortByName, role );
    
    node.getContributors().clear();
    node.getContributors().add( contributor );
}

