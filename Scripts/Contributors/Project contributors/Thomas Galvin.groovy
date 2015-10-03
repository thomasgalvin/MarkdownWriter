import com.galvin.markdown.model.ContributorRole;
import com.galvin.markdown.model.Contributor;

String name = "Thomas Galvin";
String sortByName = "Galvin, Thomas";
ContributorRole role = ContributorRole.AUTHOR;
Contributor contributor = new Contributor( name, sortByName, role );

controller.getProject().getContributors().clear();
controller.getProject().getContributors().add( contributor );

