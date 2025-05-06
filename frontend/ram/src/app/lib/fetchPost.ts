export async function fetchPost(id: string) {
  const API_POST_URL = process.env.API_POST_URL;
  const USERNAME = process.env.API_USERNAME;
  const PASSWORD = process.env.API_PASSWORD;
  const fetchPostUrl = API_POST_URL + "/" + id;

  try {
    const response = await fetch(fetchPostUrl, {
      headers: {
        Authorization:
          "Basic " + Buffer.from(`${USERNAME}:${PASSWORD}`).toString("base64"),
      },
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const post = await response.json();
    return { success: true, data: post };
  } catch (error) {
    console.error("Error fetching post:", error);
    return {
      success: false,
      error:
        error instanceof Error ? error.message : "An unknown error occurred",
    };
  }
}
