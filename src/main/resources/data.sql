INSERT INTO scc_code (code, used) VALUES
                                      ('1AZN0FXJVM', FALSE),
                                      ('JOV50TOSYR', FALSE),
                                      ('SDUBJ5IOYB', FALSE),
                                      ('YFUVLYBQZR', FALSE),
                                      ('IGBQET8OOY', FALSE),
                                      ('R2ZHBUYO2V', FALSE),
                                      ('Z9HOC1LF4X', FALSE),
                                      ('9IJKHGHJK4', FALSE),
                                      ('N5J53QK9FO', FALSE),
                                      ('ZDN06T01V9', FALSE),
                                      ('4XRDN9O4AW', FALSE),
                                      ('921664ML8D', FALSE),
                                      ('A546AKU16A', FALSE),
                                      ('V0GB2G690L', FALSE),
                                      ('12EOU5RGVX', FALSE),
                                      ('0IXYCAH8UW', FALSE),
                                      ('GKJ3K1YBGE', FALSE),
                                      ('46HJV9KH1F', FALSE),
                                      ('S6K3AV3IVR', FALSE),
                                      ('IKKSZYJTSH', FALSE);


INSERT INTO referendum (title, description, status, locked) VALUES
                                                                ('Build a new park?', 'Should Shangri-La build a new public park?', 'OPEN', TRUE),
                                                                ('Increase council tax?', 'Should council tax increase by 2%?', 'CLOSED', TRUE);

INSERT INTO referendum_option (text, referendum_id) VALUES
                                                        ('Yes', 1),
                                                        ('No', 1),
                                                        ('Yes', 2),
                                                        ('No', 2);