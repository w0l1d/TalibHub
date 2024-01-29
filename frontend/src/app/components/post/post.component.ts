import {Component} from '@angular/core';
import {PostForm, PostModalComponent} from '../post-modal/post-modal.component';
import {PostService} from '../../services/post.service';
import Poste from '../../models/poste';
import {NgForOf} from "@angular/common";
import {HttpClientModule} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-post',
  standalone: true,
  imports: [
    PostModalComponent,
    HttpClientModule,
    NgForOf
  ],
  providers: [
    PostService
  ],
  templateUrl: './post.component.html',
  styleUrl: './post.component.css'
})
export class PostComponent {

  posts?: Poste[];

  constructor(
    private postService: PostService,
    private router: Router
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

  handlePosteCreation(posteForm: PostForm): void {

    const poste: Poste = {
      titre: posteForm.titre,
      description: posteForm.description,
      image: posteForm.image
    };

    console.log("poste", poste);
    this.postService.addPoste(poste).subscribe((data: any) => {
      console.log(data);
      this.posts?.unshift(data);
    });
  }

  post():Poste{
    if(this.posts){
      return this.posts[0];
    }
    return {
      id: 'id',
      titre: 'titre',
      description: 'description',
    }
  }

  navigateToPost(id: string | undefined) {
    this.router.navigate(['/post', id]).then(() => console.log("navigated to reviews"));

  }
}
