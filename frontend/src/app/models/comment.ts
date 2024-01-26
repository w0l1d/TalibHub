import User from "./user";
import Poste from "./poste";

class Comment {
  id?: number;
  content: string;
  post?: Poste;
  user: User;
  replies?: Comment[];
  createdOn?: Date;
  lastUpdatedOn?: Date;

    constructor(id: number, content: string, user: User, post?: Poste, replies?: Comment[], createdOn?: Date, lastUpdatedOn?: Date) {
        this.id = id;
        this.content = content;
        this.post = post;
        this.user = user;
        this.replies = replies;
        this.createdOn = createdOn;
        this.lastUpdatedOn = lastUpdatedOn;
    }
}

export default Comment;