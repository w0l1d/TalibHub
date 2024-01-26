import { Component } from '@angular/core';
import { PostModalComponent } from '../post-modal/post-modal.component';
import { PostService } from '../../services/post.service';
import Poste from '../../models/poste';

@Component({
  selector: 'app-post',
  standalone: true,
  imports: [
    PostModalComponent
  ],
  templateUrl: './post.component.html',
  styleUrl: './post.component.css'
})
export class PostComponent {

  posts?: Poste[];

  constructor(
    private postService: PostService
  ) {

  }

  ngOnInit() {
    this.getPosts();
  }

  getPosts(): void {
    this.postService.getAllPosts().subscribe((data: any) => {
      console.log(data);
      this.posts = data;
    });
  }

  post():Poste{
    if(this.posts){
      return this.posts[0];
    }
    return {
      id: 1,
      titre: 'titre',
      description: 'description',
    }
  }

}
