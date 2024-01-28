import {Component} from '@angular/core';
import NavbarData from './navbar-data';
import {LayoutComponent} from '../../components/layout/layout.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {PostService} from "../../services/post.service";
import Poste from "../../models/poste";
import {ActivatedRoute} from "@angular/router";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-post-forum',
  standalone: true,
  imports: [
    LayoutComponent,
    ReactiveFormsModule,
    HttpClientModule,
    NgForOf,
    FormsModule
  ],
  providers: [
    PostService
  ],
  templateUrl: './post-forum.component.html',
  styleUrl: './post-forum.component.css'
})
export class PostForumComponent {

  navbarData = NavbarData;
  poste?: Poste;
  comment: string = '';

  constructor(
    private postService: PostService,
    private route: ActivatedRoute,
  ) {
  }

  ngOnInit() {
    this.getPosteById();
  }

  getPosteById(): void {

    this.route.params.subscribe(params => {
      const postId = params['id'];
      console.log("Post id: " + postId);

      this.postService.getPostById(postId)
        .subscribe(data => {
          this.poste = data;
          console.log(this.poste);
        });
    });
  }

  public addCommentToPost(): void {
    console.log("Comment: " + this.comment);
    if (this.comment == '')
      return;


    /*const comment : Comment = {
      content: this.comment
    }*/
    this.postService.addCommentToPost(this.poste?.id!, this.comment)
      .subscribe(data => {
        this.poste = data;
        console.log(this.poste);
      });
  }



}
