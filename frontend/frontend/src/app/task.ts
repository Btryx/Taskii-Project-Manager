export class Task {
  taskId!: string;
  taskTitle!: string;
  taskStatus!: string;
  taskPriority: number = 3;
  taskDate: Date = new Date();
  taskDesc: string | undefined;
  projectId!: string;
}
