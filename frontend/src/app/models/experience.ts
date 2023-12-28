import Institut from "./institut";

class Experience {
    title: string;
    description: string;
    startAt: Date;
    endAt: Date;
    institut: Institut;

    constructor(
        title: string, 
        description: string, 
        startAt: Date, 
        endAt: Date, 
        institut: Institut
    ) {
        this.title = title;
        this.description = description;
        this.startAt = startAt;
        this.endAt = endAt;
        this.institut = institut;
    }
}

export default Experience;
