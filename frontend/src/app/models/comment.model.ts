export interface Comment {
  id?: string;
  username: string;
  content: string;
  approved: boolean;
  createdAt?: string;
  deletionToken?: string;
  pendingContent?: string;
  timestamp?: string;
}
