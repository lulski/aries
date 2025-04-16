import AuthGuard from "@/app/components/AuthGuard";

export default function NewPostPage() {
  return (
    <AuthGuard>
      <h1>Create a New Post</h1>
      {/* You can add your form here */}
    </AuthGuard>
  );
}
