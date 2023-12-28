import Institut from "./institut";

class Education {
    title: string;
    studyField: string;
    description: string;
    startAt: Date;
    endAt: Date;
    institut: Institut;

    constructor(
        title: string,
        studyField: string,
        description: string,
        startAt: Date,
        endAt: Date,
        institut: Institut
    ) {
        this.title = title;
        this.studyField = studyField;
        this.description = description;
        this.startAt = startAt;
        this.endAt = endAt;
        this.institut = institut;
    }
}

export default Education;
