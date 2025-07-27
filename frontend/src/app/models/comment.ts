export class Comment {
  commentId!: string;
  taskId!: string;
  userId!: string;
  comment: string = "";
  createdAt!: Date;
  userName?: string;
}
