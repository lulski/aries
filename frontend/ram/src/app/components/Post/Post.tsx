"use client";
import {
  Card,
  createTheme,
  Group,
  MantineProvider,
  Text,
  Title,
} from "@mantine/core";
import { Link, RichTextEditor } from "@mantine/tiptap";
import Highlight from "@tiptap/extension-highlight";
import Superscript from "@tiptap/extension-superscript";
import TextAlign from "@tiptap/extension-text-align";
import { Color, TextStyle } from "@tiptap/extension-text-style";
import { useEditor } from "@tiptap/react";
import StarterKit from "@tiptap/starter-kit";
import { PostData } from "../../lib/definitions";

type PostProps = {
  post: PostData;
  allowHtmlMarkup: boolean;
};

const theme = createTheme({
  headings: {
    textWrap: "wrap",
  },
  components: {
    Title: {
      styles: () => ({
        root: {
          textTransform: "capitalize",
        },
      }),
    },
  },
});

export default function Post({ post, allowHtmlMarkup }: PostProps) {
  // let displayContent;

  // const sanitizedContent = sanitizeHtml(post.content, {
  //   allowedTags: [],
  //   allowedAttributes: {
  //     a: ["href", "target", "rel"],
  //   },
  // });
  // console.log("allowHtmlMarkup: " + allowHtmlMarkup);
  // if (allowHtmlMarkup) {
  //   displayContent = post.content;
  // } else {
  //   displayContent = sanitizedContent;
  // }

  const editor = useEditor({
    shouldRerenderOnTransaction: true,

    extensions: [
      StarterKit,
      TextStyle,
      Color,
      Link,
      Superscript,
      Highlight,
      TextAlign.configure({ types: ["heading", "paragraph"] }),
    ],
    content: post.content,
    immediatelyRender: false,
  });

  return (
    <MantineProvider theme={theme}>
      <Card padding="lg" styles={{ root: { marginBottom: 10 } }} id={post.id}>
        <Group justify="center" mb="md" align="right">
          <Title order={1}>{post.title}</Title>
        </Group>

        {/* <Box size="sm" dangerouslySetInnerHTML={{ __html: displayContent }} /> */}

        <RichTextEditor
          id="post"
          editor={editor}
          styles={{ content: { minHeight: 400 } }}
        >
          <RichTextEditor.Content />
        </RichTextEditor>
        <br />
        <Text size="xs" ta="right">
          {post.author}
        </Text>
        <Text size="xs" ta="right">
          {post.createdOn}
        </Text>
      </Card>
    </MantineProvider>
  );
}
