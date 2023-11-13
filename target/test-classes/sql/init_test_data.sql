TRUNCATE TABLE KNOWLEDGE RESTART IDENTITY;

INSERT INTO KNOWLEDGE (TITLE, AUTHOR, DATE_MONTH, VIEW_COUNT, LIKE_COUNT, LINK)
VALUES ('Knowledge title 1', 'Test Author 1', 'April 20', 100, 50, 'https://ted.com/talks/information_about_testing1'),
       ('Knowledge title 2', 'Test Author 2', 'May 13', 200, 150, 'https://ted.com/talks/information_about_testing2'),
       ('Knowledge title 3', 'Test Author 1', 'June 19', 10, 5, 'https://ted.com/talks/information_about_testing3'),
       ('Knowledge title 4', 'Test Author 3', 'April 21', 500, 140, 'https://ted.com/talks/information_about_testing3');
