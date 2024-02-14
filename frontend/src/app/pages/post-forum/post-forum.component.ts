import {Component, inject} from '@angular/core';
import {LayoutComponent} from '../../components/layout/layout.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {PostService} from "../../services/post.service";
import Poste from "../../models/poste";
import {ActivatedRoute} from "@angular/router";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {environment as env} from "../../../environments/environment.development";
import {AuthService} from "../../services/auth.service";
import User from "../../models/user";
import NavbarLink from "../../models/NavbarLink";

@Component({
  selector: 'app-post-forum',
  standalone: true,
  imports: [
    LayoutComponent,
    ReactiveFormsModule,
    HttpClientModule,
    NgForOf,
    FormsModule,
    DatePipe,
    NgIf
  ],
  providers: [
    PostService,
    AuthService
  ],
  templateUrl: './post-forum.component.html',
  styleUrl: './post-forum.component.css'
})
export class PostForumComponent {

  navbarData: NavbarLink[] | undefined;
  poste?: Poste;
  user?: User;
  comment: string = '';
  baseUrl: string = '';

  constructor(
    private postService: PostService,
    private authService: AuthService,
    private route: ActivatedRoute,
  ) {
    this.baseUrl = env.api;
    this.setNavbarData();

  }

  ngOnInit() {
    this.user = this.authService.getLoggedUser();
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

  private setNavbarData(): void {
    this.navbarData = [
      { route: '/home', icon: 'fa-solid fa-house', label: 'Home' , highlight: true},
      { route: '/search', icon: 'fa-solid fa-search', label: 'Search', highlight: false},
      { route: '/news', icon: 'fa-solid fa-newspaper', label: 'News', highlight: false },
      { route: '/logout', icon: 'fa-solid fa-right-from-bracket', label: 'Logout', highlight: false },
    ];

    if (inject(AuthService).isManager()) {
      this.navbarData.splice(2, 0, { route: '/studentManagement', icon: 'fa-sharp fa-solid fa-shield-halved', label: 'Admin', highlight: false });
    } else if (inject(AuthService).isStudent()) {
      this.navbarData.splice(1, 0, { route: '/studentProfile', icon: 'fa-solid fa-user', label: 'Profile', highlight: false });
    }
  }

}
