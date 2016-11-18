def classify(features_train, labels_train, method):   
    ### import the sklearn module for GaussianNB
    ### create classifier
    ### fit the classifier on the training features and labels
    ### return the fit classifier
    
    
    ### your code goes here!
    if method == 'nb':
        from sklearn.naive_bayes import GaussianNB
        clf = GaussianNB()
        clf.fit(features_train, labels_train)
        return clf
    elif method == 'lr':
        from sklearn.linear_model import LinearRegression
        clf = LinearRegression()
        clf.fit(features_train, labels_train)
        return clf
    elif method == 'dtc':
        from sklearn import tree
        clf = tree.DecisionTreeClassifier()
        clf.fit(features_train, labels_train)
        return clf
    elif method == 'svm':
        from sklearn.svm import SVC
        clf = SVC()
        clf.fit(features_train, labels_train)
        return clf
    return None
    
