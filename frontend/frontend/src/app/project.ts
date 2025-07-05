export class Project {
  projectId!: string;
  projectName!: string;
  projectDesc: string | undefined;
  createdAt!: Date;
  active: boolean = true;
  parentId: string | undefined;
  userId!: string;
}
