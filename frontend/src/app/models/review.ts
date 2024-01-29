import Student from "./student";

class Review {
    id?: string;
    review: string;
    rating: number;
    createdOn?: Date;
    institut: any;
    student?: Student;

  constructor(
    review: string,
    rating: number,
    institut: any,
    id?: string,
    createdOn?: Date,
    student?: Student
  ) {
    this.id = id;
    this.review = review;
    this.rating = rating;
    this.createdOn = createdOn;
    this.institut = institut;
    this.student = student;
  }
}

export default Review;