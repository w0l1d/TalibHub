class Experience {
    id?: string;
    title: string;
    description: string;
    startAt: string;
    endAt: string;
    institut?: any;

    constructor(
        id: string,
        title: string,
        description: string,
        startAt: string,
        endAt: string,
        institut: any
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startAt = startAt;
        this.endAt = endAt;
        this.institut = institut;
    }
}

export default Experience;
