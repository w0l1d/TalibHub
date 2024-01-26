import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostForumComponent } from './post-forum.component';

describe('PostForumComponent', () => {
  let component: PostForumComponent;
  let fixture: ComponentFixture<PostForumComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PostForumComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PostForumComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
