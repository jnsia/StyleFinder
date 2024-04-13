export interface Comment {
  id: number;

  content: string;
  createdDate: Date;
  updatedDate: Date;

  userId: number;
  feedId: number;
}
