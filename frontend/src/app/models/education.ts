class Education {
    title: string;
    studyField: string;
    description: string;
    startAt: string;
    endAt: string;
    institut?: any;

    constructor(
        title: string,
        studyField: string,
        description: string,
        startAt: string,
        endAt: string,
        institut: any
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