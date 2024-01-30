import {Component} from '@angular/core';
import {PostForm, PostModalComponent} from '../post-modal/post-modal.component';
import {PostService} from '../../services/post.service';
import Poste from '../../models/poste';
import {NgForOf, NgIf} from "@angular/common";
import {HttpClientModule} from "@angular/common/http";
import {Router} from "@angular/router";
import User from "../../models/user";
import {AuthService} from "../../services/auth.service";
import {environment as env} from "../../../environments/environment.development";

@Component({
  selector: 'app-post',
  standalone: true,
  imports: [
    PostModalComponent,
    HttpClientModule,
    NgForOf,
    NgIf
  ],
  providers: [
    PostService,
    AuthService
  ],
  templateUrl: './post.component.html',
  styleUrl: './post.component.css'
})
export class PostComponent {

  posts?: Poste[];
  user?: User;
  baseUrl: string = '';

  constructor(
    private postService: PostService,
    private authService: AuthService,
    private router: Router
  ) {
    this.baseUrl = env.api;
  }

  ngOnInit() {
    this.user = this.authService.getLoggedUser();
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
