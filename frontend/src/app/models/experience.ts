class Experience {
    title: string;
    description: string;
    startAt: string;
    endAt: string;
    institut?: any;

    constructor(
        title: string,
        description: string,
        startAt: string,
        endAt: string,
        institut: any
    ) {
        this.title = title;
        this.description = description;
        this.startAt = startAt;
        this.endAt = endAt;
        this.institut = institut;
    }
}

export default Experience;
