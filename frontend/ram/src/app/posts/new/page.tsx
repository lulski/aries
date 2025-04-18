import AuthGuard from "@/app/components/AuthGuard";
import { SessionProvider } from "@/app/context/SessionContext";
import { SessionData } from "@/app/lib/definitions";
import { getSessionData } from "@/app/lib/sessionUtil";
import { redirect } from "next/navigation";

export default async function NewPostPage() {
  console.log(">>> fetching cookie");
  const session = await getSessionData();

  return (
    <AuthGuard>
      <h1>Create a New Post {session.firstname} !</h1>
    </AuthGuard>
  );




  // const session: SessionData = await getSessionData();

  // if (!session?.isAuthenticated) {
  //   redirect("/login"); // SSR-based redirect
  // }

  // return (
  //   // <SessionProvider value={session}>
  //     <h1>Create a New Post, {session.firstname}!</h1>
  //   // </SessionProvider>
  // ); 
}
