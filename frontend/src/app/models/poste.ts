import User from "./user";
import Comment from "./comment";

class Poste{
  id?: string;
  titre: string;
  description: string;
  image?: File;
  imageUri?: string;
  user?: User;
  comments?: Comment[];
  createdOn?: Date;
  lastUpdatedOn?: Date;

  constructor(id: string, titre: string, description: string, image?: File, imageUri?: string, comments?: Comment[], createdOn?: Date, lastUpdatedOn?: Date, user?: User) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.image = image;
        this.imageUri = imageUri;
        this.user = user;
        this.comments = comments;
        this.createdOn = createdOn;
        this.lastUpdatedOn = lastUpdatedOn;
    }
    
}

export default Poste;
